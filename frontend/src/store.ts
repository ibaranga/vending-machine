import { configureStore } from "@reduxjs/toolkit";
import { productApi } from "./services/productApi";
import { userApi } from "./services/userApi";
import { vendingMachineApi } from "./services/vendingMachineApi";
import { alertOnError } from "./middleware/alertOnError";
import { loginSlice } from "./features/loginOrRegister/loginSlice";
import { alertSlice } from "./features/alert/alertSlice";

const getStore = () => {
  return configureStore({
    reducer: {
      [productApi.reducerPath]: productApi.reducer,
      [userApi.reducerPath]: userApi.reducer,
      [vendingMachineApi.reducerPath]: vendingMachineApi.reducer,
      [loginSlice.name]: loginSlice.reducer,
      [alertSlice.name]: alertSlice.reducer,
    },
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(
        productApi.middleware,
        userApi.middleware,
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
