import { Product } from "../../services/productsApi";
import React, { useMemo } from "react";
import { useBuyMutation, useGetBalanceQuery } from "../../services/vendingMachineApi";
import { Box, Button } from "@mui/material";
import { ProductGridItem } from "../product/ProductGridItem";
import { VendingMachineReceipt } from "./VendingMachineReceipt";

interface Props {
  product: Product;
  index: number;
}

export const VendingMachineProductCard: React.FC<Props> = ({ product, index }) => {
  const [buy, buyResult] = useBuyMutation();
  const getBalanceResult = useGetBalanceQuery();

  const canBuy = useMemo(
    () =>
      !buyResult.isLoading &&
      !getBalanceResult.isLoading &&
      !getBalanceResult.isFetching &&
      product.amountAvailable > 0 &&
      (getBalanceResult.data?.balance ?? 0) >= product.cost,
    [buyResult, getBalanceResult],
  );
  const onBuy = () => {
    buy({ productId: product.id, amount: 1 });
  };

  return (
    <ProductGridItem
      key={product.id}
      product={product}
      index={index}
      actions={
        <Box
          sx={{
            height: 45,
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-around",
            backgroundColor: "divider",
            padding: ".3em",
          }}
        >
          <Button
            variant={"contained"}
            id={`buy-product-${product.productName}`}
            onClick={onBuy}
            size={"large"}
            disabled={!canBuy}
          >
            Buy
          </Button>
          {buyResult.data ? <VendingMachineReceipt buyResponse={buyResult.data} /> : null}
        </Box>
      }
    />
  );
};
