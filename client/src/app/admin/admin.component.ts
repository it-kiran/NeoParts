import { Component, OnInit, ElementRef } from '@angular/core';
import { AdminService } from './admin.service';
import { Product, Brand, Category, Model } from '../product-page/product-page.component';
import { GlobalService } from '../global-service.service';
import { BackendService } from '../services/backend.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  constructor(private adminService: AdminService, private element: ElementRef, private globalService: GlobalService, private backendService: BackendService) { }

  productViewList: Product[] = []
  ngOnInit() {

    this.getProductDetails();

  }

  getProductDetails(){

    this.backendService.getAllProducts()
    .subscribe((product)=>{
      this.productViewList = product;
    })
  }

  


}
