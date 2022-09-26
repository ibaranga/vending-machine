import React from "react";
import { Grid } from "@mui/material";
import { useGetProductsQuery } from "../../services/productApi";
import { VendingMachineProductCard } from "./VendingMachineProductCard";

export const VendingMachineMenu: React.FC = () => {
  const { data, isLoading, isError } = useGetProductsQuery({ page: 0, size: 10 });

  return (
    <Grid container flexGrow={1} spacing={"1em"}>
      {(data?.products ?? []).map((product, index) => (
        <VendingMachineProductCard key={product.id} product={product} index={index} />
      ))}
    </Grid>
  );
};
