import React from "react";
import { VendingMachineMenu } from "../vendingMachine/VendingMachineMenu";
import { useLoggedInUser } from "../../hooks/useLoggedInUser";
import { UserRole } from "../../services/usersApi";
import { Navigate } from "react-router-dom";
import { VendingMachineAccount } from "../vendingMachine/VendingMachineAccount";
import { Box } from "@mui/material";

export const BuyerHome: React.FC = () => {
  const [user] = useLoggedInUser();
  if (!user || user.role !== UserRole.BUYER) {
    return <Navigate to={"/"} />;
  }

  return (
    <Box display={"flex"} flexDirection={"column"} gap={"2em"} width={"100%"} alignItems={"center"}>
      <VendingMachineAccount />
      <VendingMachineMenu />
    </Box>
  );
};
