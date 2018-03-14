import { Component, OnInit } from '@angular/core';

import { Http,Response } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import {BackendService} from '../../services/backend.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit {
productList: Product[] = [];
  constructor(private http:Http,private backendService:BackendService,private router: Router) { }

  ngOnInit() {

      console.log(this.backendService.appleInfo);
      this.getProductByModelId(111);
  }

  getProductByModelId(modelId: number){
    this.backendService.getProductByModelId(modelId)
    .subscribe((product:Product[]) =>{
      this.productList = product;
      console.log('product list', this.productList);
    })
  }

}

export class Product {
  productNo: string;
  description: string;
  categoryId: number;
  brandId: number;
  vendorId: number;
  modelId: number;
  cost: number;
  retail: number;
  quantity: number;
  ecommerce: boolean;
  tax:boolean
  image: any;
}
