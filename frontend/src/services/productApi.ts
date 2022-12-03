import { createApi } from "@reduxjs/toolkit/query/react";
import { fetchBaseQueryWithReauth, isNon5xxStatusCodeResponse } from "./common";

export const productApi = createApi({
  reducerPath: "productApi",
  baseQuery: fetchBaseQueryWithReauth({
    baseUrl: "/api/product",
  }),
  tagTypes: ["inventory"],
  endpoints: (builder) => ({
    getProducts: builder.query<GetProductsResponse, GetProductsRequest>({
      query: (params: GetProductsRequest) => ({
        url: "",
        method: "GET",
        params,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      providesTags: ["inventory"],
    }),
    getSellerProducts: builder.query<GetProductsResponse, GetSellerProductsRequest>({
      query: (params) => ({
        url: "/seller",
        method: "GET",
        params,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      providesTags: ["inventory"],
    }),
    createProduct: builder.mutation<void, CreateProductRequest>({
      query: (body: CreateProductRequest) => ({
        url: "",
        method: "POST",
        body,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      invalidatesTags: ["inventory"],
    }),
    updateProduct: builder.mutation<void, UpdateProductRequest & { id: string }>({
      query: ({ id, ...body }) => ({
        url: `/${id}`,
        method: "PUT",
        body,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      invalidatesTags: ["inventory"],
    }),
    deleteProduct: builder.mutation<void, DeleteProductRequest>({
      query: ({ id }) => ({
        url: `/${id}`,
        method: "DELETE",
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      invalidatesTags: ["inventory"],
    }),
  }),
});

interface ProductData {
  readonly productName: string;
  readonly amountAvailable: number;
  readonly cost: number;
}

export interface Product extends ProductData {
  readonly id: string;
  readonly sellerId: string;
}

export type CreateProductRequest = ProductData;

export type UpdateProductRequest = Partial<ProductData>;

export interface DeleteProductRequest {
  id: string;
}

export interface GetProductsRequest {
  readonly page: number;
  readonly size: number;
}

export type GetSellerProductsRequest = GetProductsRequest;

export interface GetProductsResponse {
  readonly products: Product[];
}

export const {
  useGetProductsQuery,
  useGetSellerProductsQuery,
  useCreateProductMutation,
  useUpdateProductMutation,
  useDeleteProductMutation,
} = productApi;
