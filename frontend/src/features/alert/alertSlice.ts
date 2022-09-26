import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface AlertState {
  message: string;
  severity: "success" | "info" | "warning" | "error";
}

const initialState: AlertState = {
  message: "",
  severity: "info",
};

export const alertSlice = createSlice({
  name: "alert",
  initialState,
  reducers: {
    setAlert: (state, action: PayloadAction<AlertState>) => {
      state.severity = action.payload.severity;
      state.message = action.payload.message;
    },
    clearAlert: (state) => {
      state.message = "";
    },
  },
});

export const { setAlert, clearAlert } = alertSlice.actions;
