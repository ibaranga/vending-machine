import React from "react";
import { Coins } from "../services/vendingMachineApi";
import { Avatar, Typography } from "@mui/material";

export interface Props {
  readonly value: Coins;
}

export const CoinAvatar: React.FC<Props> = ({ value }) => (
  <Avatar sx={{ width: 50, height: 50, backgroundColor: "primary.main" }}>
    <Typography fontFamily={"monospace"} fontSize={14}>
      ¢{value}
    </Typography>
  </Avatar>
);
