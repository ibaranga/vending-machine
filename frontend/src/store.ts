import { configureStore } from "@reduxjs/toolkit";
import { productsApi } from "./services/productsApi";
import { usersApi } from "./services/usersApi";
import { vendingMachineApi } from "./services/vendingMachineApi";
import { alertOnError } from "./middleware/alertOnError";
import { loginSlice } from "./features/loginOrRegister/loginSlice";
import { alertSlice } from "./features/alert/alertSlice";

const getStore = () => {
  return configureStore({
    reducer: {
      [productsApi.reducerPath]: productsApi.reducer,
      [usersApi.reducerPath]: usersApi.reducer,
      [vendingMachineApi.reducerPath]: vendingMachineApi.reducer,
      [loginSlice.name]: loginSlice.reducer,
      [alertSlice.name]: alertSlice.reducer,
    },
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(
        productsApi.middleware,
        usersApi.middleware,
        vendingMachineApi.middleware,
        alertOnError,
      ),
  });
};

export const store = getStore();

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;
