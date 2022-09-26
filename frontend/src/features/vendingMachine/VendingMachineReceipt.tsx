import React, { useState } from "react";
import { BuyResponse } from "../../services/vendingMachineApi";
import { Box, Dialog, DialogTitle, Paper, styled, Typography } from "@mui/material";
import { CoinAvatar } from "../../common/CoinAvatar";
import { Money } from "../../common/Money";

interface Props {
  buyResponse: BuyResponse;
}

const FlexBox = styled(Box)({
  display: "flex",
  flexDirection: "row",
  justifyContent: "space-between",
});

export const VendingMachineReceipt: React.FC<Props> = ({ buyResponse }) => {
  const [open, setOpen] = useState(true);

  return (
    <Dialog open={open} onClose={() => setOpen(false)}>
      <DialogTitle>Receipt</DialogTitle>
      <Paper
        sx={{
          minWidth: 300,
          minHeight: 400,
          display: "flex",
          flexDirection: "column",
          gap: "2em",
          padding: "2em",
        }}
      >
        <FlexBox>
          <Typography>Total Spent Amount:</Typography>
          <Money value={buyResponse.totalSpentAmount} currency={"$"} />
        </FlexBox>
        <FlexBox>
          <Typography>Purchased Product:</Typography>
          <Typography>{buyResponse.purchasedProductName}</Typography>
        </FlexBox>
        <Box flexGrow={1} />
        <Typography>Change</Typography>
        <Box sx={{ display: "flex", flexFlow: "row wrap", gap: "1em" }}>
          {buyResponse.change.map((coin, index) => (
            <CoinAvatar key={index} value={coin} />
          ))}
        </Box>
      </Paper>
    </Dialog>
  );
};
