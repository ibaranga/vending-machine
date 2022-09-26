import React, { Children, useMemo } from "react";
import {
  Coins,
  useDepositMutation,
  useGetBalanceQuery,
  useResetMutation,
} from "../../services/vendingMachineApi";
import { Box, Button, Card, CardActions, CardHeader, IconButton } from "@mui/material";
import { CoinAvatar } from "../../common/CoinAvatar";
import { RestartAltOutlined } from "@mui/icons-material";
import { Money } from "../../common/Money";
import { HighlightAfterUpdate } from "../../common/HighlightAfterUpdate";

function isCoins(coins: Coins | string): coins is Coins {
  return Number.isFinite(coins);
}

export const VendingMachineAccount: React.FC = () => {
  const [deposit, depositResult] = useDepositMutation();
  const [reset, resetResult] = useResetMutation();
  const getBalanceResult = useGetBalanceQuery();

  const isLoading = useMemo(
    () =>
      [getBalanceResult, resetResult, getBalanceResult].some((state) => state.isLoading) ||
      getBalanceResult.isFetching,
    [depositResult, resetResult, getBalanceResult],
  );

  const depositCoins = (coins: Coins) => {
    deposit({ coins });
  };

  const resetAccount = () => {
    reset();
  };

  const coinsDepositActions = Object.values(Coins)
    .filter(isCoins)
    .map((coin) => (
      <Button
        disabled={isLoading}
        id={`deposit-coin-${coin}`}
        size={"large"}
        variant={"text"}
        key={coin}
        onClick={() => depositCoins(coin)}
      >
        <CoinAvatar value={coin} />
      </Button>
    ));

  const balance = (
    <Box sx={{ height: "20px", display: "flex", alignItems: "center" }}>
      <HighlightAfterUpdate updating={isLoading}>
        <Money value={getBalanceResult.data?.balance ?? 0} currency={"$"} />
      </HighlightAfterUpdate>
    </Box>
  );
  return (
    <Card>
      <CardHeader id={"balance-value"} title={"Balance"} subheader={balance} />
      <CardActions sx={{ display: "flex", flexDirection: "column" }}>
        <Box
          sx={{
            width: "100%",
            display: "flex",
            flexDirection: "auto",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Box sx={{ display: "flex", flexDirection: "row", alignItems: "center" }}>
            <span>{Children.toArray(coinsDepositActions)}</span>
            <IconButton disabled={isLoading} onClick={resetAccount}>
              <RestartAltOutlined />
            </IconButton>
          </Box>
        </Box>
      </CardActions>
    </Card>
  );
};
