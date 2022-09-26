import { Product } from "../../services/productApi";

export interface ProductRows {
  products: ProductRow[];
}

export interface ProductRow {
  productId?: string;
  index: number;
  productName: string;
  amountAvailable: number;
  cost: number;
}

export interface ProductGridRowProps {
  index: number;
  editMode?: boolean;
}

export function toProductRow(product: Product, index: number): ProductRow {
  return {
    productId: product.id,
    index,
    productName: product.productName,
    amountAvailable: product.amountAvailable,
    cost: product.cost,
  };
}
