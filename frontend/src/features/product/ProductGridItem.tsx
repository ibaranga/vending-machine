import { Box, Grid, Paper, Typography } from "@mui/material";
import { Money } from "../../common/Money";
import React from "react";
import { Product } from "../../services/productsApi";

interface Props {
  product: Product;
  index: number;
  actions: React.ReactNode;
}

export const ProductGridItem: React.FC<Props> = ({ product, actions }) => {
  return (
    <Grid item xs={12} sm={6} md={4} lg={3} xl={2}>
      <Paper
        sx={{
          // margin: "1em",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: "1em",
        }}
      >
        <Box
          sx={{
            width: "100%",
            padding: ".5em 1em .5em 1em",
            backgroundColor: "primary.main",
            color: "primary.contrastText",
            textAlign: "center",
          }}
        >
          <Typography variant={"h5"}>{product.productName}</Typography>
        </Box>
        <Money value={product.cost} currency={"$"} />

        <Box sx={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
          {product.amountAvailable ? (
            <Typography>{product.amountAvailable} in stock</Typography>
          ) : (
            <Typography>Out of stock</Typography>
          )}
        </Box>

        <Box width={"100%"}>{actions}</Box>
      </Paper>
    </Grid>
  );
};
