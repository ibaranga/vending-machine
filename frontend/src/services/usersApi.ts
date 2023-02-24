import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const usersApi = createApi({
  reducerPath: "usersApi",
  baseQuery: fetchBaseQuery({
    baseUrl: "/api/users",
  }),
  endpoints: (builder) => ({
    createUser: builder.mutation<void, CreateUserRequest>({
      query: (body: CreateUserRequest) => ({
        url: "",
        method: "POST",
        body,
      }),
    }),
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (body: LoginRequest) => ({
        url: "/login",
        method: "POST",
        body,
      }),
    }),
    refresh: builder.mutation<TokenPair, RefreshToken>({
      query: (body: RefreshToken) => ({
        url: "/refresh",
        method: "POST",
        body,
      }),
    }),
    logout: builder.mutation<void, RefreshToken>({
      query: (body: RefreshToken) => ({
        url: "/logout",
        method: "POST",
        body,
      }),
    }),
    logoutAll: builder.mutation<void, RefreshToken>({
      query: (body: RefreshToken) => ({
        url: "/logout/all",
        method: "POST",
        body,
      }),
    }),
  }),
});

export enum UserRole {
  BUYER = "buyer",
  SELLER = "seller",
}

export interface CreateUserRequest {
  readonly username: string;
  readonly password: string;
  readonly role: UserRole;
}

export interface LoginRequest {
  readonly username: string;
  readonly password: string;
}

export interface LoginResponse extends TokenPair {
  readonly numActiveSessions?: number;
}

export interface UserTokenClaims {
  readonly role: UserRole;
}

export interface TokenPair extends AccessToken, RefreshToken {}

export interface AccessToken {
  readonly accessToken: string;
}

export interface RefreshToken {
  readonly refreshToken: string;
}

export interface User extends LoginResponse, UserTokenClaims {}

export const {
  useCreateUserMutation,
  useLoginMutation,
  useRefreshMutation,
  useLogoutMutation,
  useLogoutAllMutation,
} = usersApi;
