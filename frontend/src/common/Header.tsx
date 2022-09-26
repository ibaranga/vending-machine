import React from "react";
import { AppBar, Avatar, Box, Typography } from "@mui/material";
import { LogoutButton } from "./LogoutButton";

export const Header: React.FC = () => {
  return (
    <AppBar
      position={"sticky"}
      sx={{
        display: "flex",
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        padding: ".5em 1em .5em 1em",
        gap: "1em",
      }}
    >
      <Avatar sx={{ width: "35px", height: "35px", backgroundColor: "primary.main" }}>
        <Typography fontFamily={"monospace"}>VM</Typography>
      </Avatar>
      <Typography variant={"h5"} fontFamily={"monospace"}>
        Vending Machine
      </Typography>
      <Box flexGrow={1} />
      <LogoutButton />
    </AppBar>
  );
};
