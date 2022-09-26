import React, { useEffect } from "react";
import { useLoggedInUser } from "../hooks/useLoggedInUser";
import { Button } from "@mui/material";
import { useLogoutMutation } from "../services/userApi";

export const LogoutButton: React.FC = () => {
  const [user, setUser] = useLoggedInUser();
  const [logout, logoutResult] = useLogoutMutation();

  useEffect(() => {
    if (logoutResult.isSuccess || logoutResult.isError) {
      setUser(null);
    }
  }, [logoutResult.isSuccess, logoutResult.isError]);

  if (!user) {
    return null;
  }

  const onLogout = () => {
    logout({ refreshToken: user.refreshToken });
  };
  return (
    <Button id={"logout"} variant={"contained"} onClick={onLogout}>
      Logout
    </Button>
  );
};
