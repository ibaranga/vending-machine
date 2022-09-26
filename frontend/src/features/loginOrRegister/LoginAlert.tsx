import React, { useEffect, useState } from "react";
import { useLoggedInUser } from "../../hooks/useLoggedInUser";
import { Box, Button, Dialog, DialogTitle, Paper, Typography } from "@mui/material";
import { useLogoutAllMutation } from "../../services/userApi";
import { useDispatch } from "react-redux";
import { AppDispatch } from "../../store";
import { setAlert } from "../alert/alertSlice";

export const LoginAlert: React.FC = () => {
  const [user] = useLoggedInUser();
  const [open, setOpen] = useState(false);
  const [logoutAll, logoutAllResult] = useLogoutAllMutation();
  const dispatch: AppDispatch = useDispatch();

  useEffect(() => {
    setOpen(!!user?.numActiveSessions && user?.numActiveSessions > 1);
  }, [user]);

  useEffect(() => {
    if (logoutAllResult.isSuccess) {
      dispatch(
        setAlert({
          severity: "info",
          message: "You were successfully logged out from everywhere!",
        }),
      );
    }
    if (logoutAllResult.isError) {
      dispatch(setAlert({ severity: "warning", message: "Failed to logout!" }));
    }
    setOpen((prev) => prev && !logoutAllResult.isSuccess && !logoutAllResult.isError);
  }, [logoutAllResult.isSuccess, logoutAllResult.isError]);

  const onClose = () => {
    setOpen(false);
  };
  const onLogoutAll = () => {
    if (user) {
      logoutAll({ refreshToken: user.refreshToken });
    }
  };

  return (
    <Dialog id={"multiple-sessions-alert"} open={open} onClose={() => setOpen(false)}>
      <DialogTitle>Warning</DialogTitle>
      <Paper sx={{ padding: "1em", height: 180, display: "flex", flexDirection: "column" }}>
        <Typography>There is already an active session using your account</Typography>
        <Box flexGrow={1} />
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
            gap: "1em",
            pt: "1em",
          }}
        >
          <Button onClick={onClose}>Ignore</Button>
          <Button id={"logout-everywhere"} variant={"outlined"} onClick={onLogoutAll}>
            Logout Everywhere
          </Button>
        </Box>
      </Paper>
    </Dialog>
  );
};
