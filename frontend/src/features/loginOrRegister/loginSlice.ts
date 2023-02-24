import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { TokenPair, User } from "../../services/usersApi";

export interface LoginState {
  user?: User;
}

const LOCAL_STORAGE_KEY = "redux.loginState";

// TODO consider using a middleware
const getInitialState: () => LoginState = () => {
  try {
    const rawState = localStorage.getItem(LOCAL_STORAGE_KEY);
    if (rawState) {
      const state: LoginState = JSON.parse(rawState) as LoginState;
      if (state.user && state.user.accessToken && state.user.refreshToken && state.user.role) {
        return {
          user: {
            role: state.user.role,
            accessToken: state.user.accessToken,
            refreshToken: state.user.refreshToken,
          },
        };
      }
    }
  } catch (ignore) {
    // ignore
  }

  localStorage.removeItem(LOCAL_STORAGE_KEY);
  return {};
};

export const loginSlice = createSlice({
  name: "login",
  initialState: getInitialState,
  reducers: {
    setLoggedInUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
      localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(state));
    },
    removeLoggedInUser: (state) => {
      delete state.user;
      localStorage.removeItem(LOCAL_STORAGE_KEY);
    },
    setTokenPair: (state, action: PayloadAction<TokenPair>) => {
      if (state.user) {
        state.user = {
          ...state.user,
          ...action.payload,
        };
      }
      localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(state));
    },
  },
});

export const { setLoggedInUser, removeLoggedInUser, setTokenPair } = loginSlice.actions;
