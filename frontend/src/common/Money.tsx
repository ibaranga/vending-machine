import React from "react";
import { Typography } from "@mui/material";

export interface Props {
  value: number;
  currency: string;
}

export const Money: React.FC<Props> = ({ value, currency }) => {
  return (
    <Typography display={"inline"} fontWeight={"bold"}>
      {currency} {value.toFixed(2)}
    </Typography>
  );
};
