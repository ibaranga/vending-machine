import React, { useState } from "react";
import {
  Product,
  useCreateProductMutation,
  useGetSellerProductsQuery,
  useUpdateProductMutation,
} from "../../services/productApi";
import { Box, Dialog, DialogTitle, Grid, IconButton, Typography } from "@mui/material";
import { AddOutlined, EditOutlined } from "@mui/icons-material";
import { ProductForm, ProductFormModel } from "./ProductForm";
import { ProductGridItem } from "./ProductGridItem";

interface DialogState {
  product?: Product;
  index: number;
}

export const ProductGrid: React.FC = () => {
  const { data, isFetching, isLoading } = useGetSellerProductsQuery({ page: 0, size: 10 });
  const [createProduct] = useCreateProductMutation();
  const [updateProduct] = useUpdateProductMutation();

  const [dialogState, setDialogState] = useState<DialogState | null>(null);

  const onSubmit = (data: ProductFormModel) => {
    if (dialogState?.product) {
      updateProduct({ id: dialogState?.product?.id, ...data });
    } else {
      createProduct(data);
    }
    setDialogState(null);
  };

  const products: Product[] = data?.products ?? [];
  return (
    <Box
      sx={{
        display: "flex",
        width: "100%",
        flexDirection: "column",
        gap: "2em",
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Typography variant={"h4"}>Products</Typography>
        <IconButton id={"add-product"} onClick={() => setDialogState({ index: products.length })}>
          <AddOutlined />
        </IconButton>
      </Box>

      <Grid container title={"Products"} flexGrow={1} spacing={"1em"}>
        {products.map((product, index) => (
          <ProductGridItem
            key={product.id}
            product={product}
            index={index}
            actions={
              <IconButton
                id={`edit-product-${index}`}
                onClick={() => setDialogState({ index, product })}
              >
                <EditOutlined />
              </IconButton>
            }
          />
        ))}
      </Grid>
      {dialogState ? (
        <Dialog open={!!dialogState} onClose={() => setDialogState(null)}>
          <DialogTitle>{dialogState.product ? "Edit Product" : "Add Product"}</DialogTitle>
          <ProductForm {...dialogState} submitHandler={onSubmit} />
        </Dialog>
      ) : null}
    </Box>
  );
};
