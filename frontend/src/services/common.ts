import { BaseQueryApi, BaseQueryFn } from "@reduxjs/toolkit/dist/query/baseQueryTypes";
import { RootState } from "../store";
import { FetchArgs, fetchBaseQuery, FetchBaseQueryError } from "@reduxjs/toolkit/query/react";
import { removeLoggedInUser, setTokenPair } from "../features/loginOrRegister/loginSlice";
import { TokenPair, User } from "./userApi";

export function isNon5xxStatusCodeResponse(response: Response) {
  return !is5xxStatusCode(response?.status);
}

export function is5xxStatusCode(statusCode: number) {
  return String(statusCode).startsWith("5");
}

export function is4xxStatusCode(statusCode: number) {
  return String(statusCode).startsWith("4");
}

export function prepareHeadersWithToken(headers: Headers, opts: Pick<BaseQueryApi, "getState">) {
  const token = (opts.getState() as RootState).login.user?.accessToken;
  if (token) {
    headers.set("authorization", `Bearer ${token}`);
  }
  return headers;
}

export function fetchBaseQueryWithReauth({ baseUrl }: { baseUrl: string }) {
  const setOfStatusCodesToAttemptRefresh = new Set([401, 403]);

  const userApiBaseQuery = fetchBaseQuery({ baseUrl: "/api/user" });

  const wrappedBaseQuery = fetchBaseQuery({ baseUrl, prepareHeaders: prepareHeadersWithToken });

  const baseQueryWithReauth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
    args,
    api,
    extraOptions,
  ) => {
    const result = await wrappedBaseQuery(args, api, extraOptions);
    const status: number = result.meta?.response?.status ?? 0;

    if (!setOfStatusCodesToAttemptRefresh.has(status)) {
      return result;
    }

    const user: User | undefined = (api.getState() as RootState).login.user;
    if (!user) {
      return result;
    }

    const refreshResult = await userApiBaseQuery(
      {
        url: "/refresh",
        method: "post",
        body: { refreshToken: user.refreshToken },
      },
      api,
      extraOptions,
    );

    if (refreshResult.data) {
      api.dispatch(setTokenPair(refreshResult.data as TokenPair));
      return wrappedBaseQuery(args, api, extraOptions);
    }

    api.dispatch(removeLoggedInUser());
    return result;
  };
  return baseQueryWithReauth;
}
