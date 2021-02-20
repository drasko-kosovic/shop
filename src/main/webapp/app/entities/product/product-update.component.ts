import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProduct, Product } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/product-category.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  productcategories: IProductCategory[] = [];

  editForm = this.fb.group({
    id: [],
    sku: [],
    name: [],
    description: [],
    unitPrice: [],
    imageUrl: [],
    active: [],
    unitsInStock: [],
    dateCreated: [],
    lastUpdated: [],
    productCategory: [],
  });

  constructor(
    protected productService: ProductService,
    protected productCategoryService: ProductCategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      if (!product.id) {
        const today = moment().startOf('day');
        product.dateCreated = today;
        product.lastUpdated = today;
      }

      this.updateForm(product);

      this.productCategoryService.query().subscribe((res: HttpResponse<IProductCategory[]>) => (this.productcategories = res.body || []));
    });
  }

  updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      sku: product.sku,
      name: product.name,
      description: product.description,
      unitPrice: product.unitPrice,
      imageUrl: product.imageUrl,
      active: product.active,
      unitsInStock: product.unitsInStock,
      dateCreated: product.dateCreated ? product.dateCreated.format(DATE_TIME_FORMAT) : null,
      lastUpdated: product.lastUpdated ? product.lastUpdated.format(DATE_TIME_FORMAT) : null,
      productCategory: product.productCategory,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  private createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      unitPrice: this.editForm.get(['unitPrice'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      active: this.editForm.get(['active'])!.value,
      unitsInStock: this.editForm.get(['unitsInStock'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value
        ? moment(this.editForm.get(['dateCreated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? moment(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      productCategory: this.editForm.get(['productCategory'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IProductCategory): any {
    return item.id;
  }
}
