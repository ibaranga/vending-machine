import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store";
import { AlertState, clearAlert } from "./alertSlice";
import { Alert, Snackbar } from "@mui/material";
import React from "react";

export const AppAlert: React.FC = () => {
  const dispatch = useDispatch();
  const alert = useSelector<RootState, AlertState>((root) => root.alert);

  const onClose = () => {
    dispatch(clearAlert());
  };

  return (
    <Snackbar open={!!alert.message} autoHideDuration={3000} onClose={onClose}>
      <Alert sx={{ width: "100%" }} severity={alert.severity}>
        {alert.message}
      </Alert>
    </Snackbar>
  );
};
