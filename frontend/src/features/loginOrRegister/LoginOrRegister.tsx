import React from "react";
import { Box, Tab, Tabs } from "@mui/material";
import { Login } from "./Login";
import { Register } from "./Register";
import { useNavigate } from "react-router";

export enum TabIndex {
  LOGIN = 0,
  REGISTER = 1,
}

interface Props {
  index: TabIndex;
}

export const LoginOrRegister: React.FC<Props> = ({ index }) => {
  const navigate = useNavigate();
  return (
    <Box>
      <Tabs centered value={index}>
        <Tab onClick={() => navigate("/login")} id={"home-tab-login"} value={0} label="Login" />
        <Tab
          onClick={() => navigate("/register")}
          id={"home-tab-register"}
          value={1}
          label="Register"
        />
      </Tabs>
      <div hidden={index !== TabIndex.LOGIN}>{index === TabIndex.LOGIN && <Login />}</div>
      <div hidden={index !== TabIndex.REGISTER}>{index === TabIndex.REGISTER && <Register />}</div>
    </Box>
  );
};
