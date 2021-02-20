import { Moment } from 'moment';
import { IProductCategory } from 'app/shared/model/product-category.model';

export interface IProduct {
  id?: number;
  sku?: string;
  name?: string;
  description?: string;
  unitPrice?: number;
  imageUrl?: string;
  active?: number;
  unitsInStock?: number;
  dateCreated?: Moment;
  lastUpdated?: Moment;
  productCategory?: IProductCategory;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public sku?: string,
    public name?: string,
    public description?: string,
    public unitPrice?: number,
    public imageUrl?: string,
    public active?: number,
    public unitsInStock?: number,
    public dateCreated?: Moment,
    public lastUpdated?: Moment,
    public productCategory?: IProductCategory
  ) {}
}
