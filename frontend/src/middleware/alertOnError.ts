import { isRejectedWithValue, Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { setAlert } from "../features/alert/alertSlice";
import { is5xxStatusCode } from "../services/common";

export const alertOnError: Middleware = (api: MiddlewareAPI) => (next) => (action) => {
  if (
    isRejectedWithValue(action) &&
    is5xxStatusCode(action.payload.originalStatus ?? action.payload.status ?? 500)
  ) {
    api.dispatch(
      setAlert({ severity: "warning", message: action.payload?.data?.error ?? "Internal error" }),
    );
  }
  return next(action);
};
