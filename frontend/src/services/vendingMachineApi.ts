import { createApi } from "@reduxjs/toolkit/query/react";
import { fetchBaseQueryWithReauth, isNon5xxStatusCodeResponse } from "./common";

export const vendingMachineApi = createApi({
  reducerPath: "vendingMachine",
  baseQuery: fetchBaseQueryWithReauth({
    baseUrl: "/api/vendingmachine",
  }),
  tagTypes: ["balance", "inventory"],
  endpoints: (builder) => ({
    getBalance: builder.query<GetBalanceResponse, void>({
      query: () => ({
        url: "/balance",
        method: "GET",
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      providesTags: ["balance"],
    }),
    deposit: builder.mutation<void, DepositRequest>({
      query: (body: DepositRequest) => ({
        url: "/deposit",
        method: "POST",
        body,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      invalidatesTags: ["balance"],
    }),
    buy: builder.mutation<BuyResponse, BuyRequest>({
      query: (body: BuyRequest) => ({
        url: "/buy",
        method: "POST",
        body,
        validateStatus: isNon5xxStatusCodeResponse,
      }),
      invalidatesTags: ["balance", "inventory"],
    }),
    reset: builder.mutation<void, void>({
      query: () => ({ url: "/reset", method: "POST", validateStatus: isNon5xxStatusCodeResponse }),
      invalidatesTags: ["balance"],
    }),
  }),
});

export enum Coins {
  FIVE = 5,
  TEN = 10,
  TWENTY = 20,
  FIFTY = 50,
  HUNDRED = 100,
}

export interface Product {
  readonly id: string;
  readonly sellerId: string;
}

export interface DepositRequest {
  readonly coins: Coins;
}

export interface BuyRequest {
  readonly productId: string;
  readonly amount: number;
}

export interface BuyResponse {
  readonly totalSpentAmount: number;
  readonly purchasedProductName: string;
  readonly change: Coins[];
}

export interface GetBalanceResponse {
  readonly balance: number;
}

export const { useDepositMutation, useGetBalanceQuery, useBuyMutation, useResetMutation } =
  vendingMachineApi;
