import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { BackendService } from '../services/backend.service';
import { Router } from '@angular/router';
import { GlobalService } from '../global-service.service';

@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.css']
})
export class ProductPageComponent implements OnInit {
  productList: Product[] = [];
  menuDto: MenuDto[] = [];
  categoryList: Category[] = [];
  cartCount: number = 0;
  alok: Product[] = [];

  constructor(private http:Http,private globalService:GlobalService, private backendService: BackendService,private router: Router) { }
  
    ngOnInit() {
        // console.log(this.backendService.appleInfo);
        this.getProductByModelId(111);
        this.getMenuDetails();
    }
  
    getProductByModelId(modelId: number){
      this.backendService.getProductByModelId(modelId)
      .subscribe((product) =>{
        this.productList = product;
        //console.log('product list', this.productList);
      })
    }
  
    getMenuDetails(){
  
      this.backendService.getData()
      .subscribe((data)=>{
        //console.log('menu', data);
  
        for(var i =0;i<data.categoryDtoList.length;i++){
          var str = data.categoryDtoList[i].name;
          //console.log('str', str);
  
          this.categoryList.push(data.categoryDtoList[i]);
        }
        this.categoryList = this.categoryList.slice();
  })
    }
  
    addProductToCart(product: Product){
     // console.log('product', product);
      product.customerPhoneNo = '7707030801';
      product.status = 'Online';
      product.date = '2018-03-11 09:41:27';
    
      this.globalService.addProductToCart(product);

  

      
      //console.log('added cart product List', this.backendService.productList);
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
    saleQuantity: number;
    ecommerce: boolean;
    tax:boolean
    image: any;
    customerPhoneNo: string;
    date: any;
    status: any;
  }
  
  
  
  export class Category {
    name: string;
    categoryId: number;
  }
  export class Brand {
    name: string;
    brandId: number;
  }
  export class Model{
    name: string;
    modelId: number;
  }
  
  export class MenuDto{
    categoryDtoList: Category[];
    webBrandDtoList: Brand[];
    modelDtoList: Model[];
  }
  