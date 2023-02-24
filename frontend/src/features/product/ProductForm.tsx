import { Product } from "../../services/productsApi";
import React from "react";
import { Box, Button, Card, CardActions, CardHeader, TextField } from "@mui/material";
import { Controller, SubmitHandler, useForm } from "react-hook-form";

interface Props {
  product?: Product;
  index: number;
  submitHandler: SubmitHandler<ProductFormModel>;
}

export type ProductFormModel = Omit<Product, "id" | "sellerId">;

export const ProductForm: React.FC<Props> = ({ product, index, submitHandler }) => {
  const {
    control,
    handleSubmit,
    formState: { errors, isValid, isDirty, isValidating },
  } = useForm<ProductFormModel>({
    defaultValues: product ?? {
      productName: "",
      amountAvailable: 0,
      cost: 0.0,
    },
    mode: "onChange",
    reValidateMode: "onChange",
    delayError: 1000,
  });

  return (
    <form onSubmit={handleSubmit(submitHandler)}>
      <Card sx={{ minWidth: 200 }}>
        <CardHeader
          title={
            <Controller
              name={"productName"}
              control={control}
              rules={{ minLength: 2, maxLength: 64, required: true }}
              render={({ field }) => (
                <TextField
                  id={`product-${index}-productName-editor`}
                  sx={{ width: "100%" }}
                  label={"Product Name"}
                  error={!!errors.productName}
                  helperText={errors.productName ? "2-64 characters" : " "}
                  {...field}
                />
              )}
            />
          }
          subheader={
            <Box
              sx={{
                display: "flex",
                flexDirection: "row",
                justifyContent: "space-between",
                gap: 1,
              }}
            >
              <Controller
                name={"cost"}
                control={control}
                rules={{ min: 0.0, required: true, pattern: /^[0-9]+(\.[0-9]{1,2})?$/ }}
                render={({ field }) => (
                  <TextField
                    id={`product-${index}-cost-editor`}
                    label={"Cost"}
                    type={"number"}
                    prefix={"$"}
                    error={!!errors.cost}
                    helperText={errors.cost?.message ?? " "}
                    {...field}
                  />
                )}
              />
              <Controller
                name={"amountAvailable"}
                control={control}
                rules={{ min: 0, required: true }}
                render={({ field }) => (
                  <TextField
                    id={`product-${index}-amountAvailable-editor`}
                    label={"Amount Available"}
                    error={!!errors.amountAvailable}
                    helperText={errors.amountAvailable?.message ?? " "}
                    {...field}
                  />
                )}
              />
            </Box>
          }
        />
        <CardActions sx={{ height: 90 }}>
          <Button variant={"contained"} type={"submit"} id={`save-product-${index}`}>
            Save
          </Button>
        </CardActions>
      </Card>
    </form>
  );
};
