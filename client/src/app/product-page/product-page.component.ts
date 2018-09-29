import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { BackendService } from '../services/backend.service';
import { Router, Route, Routes, ActivatedRoute } from '@angular/router';
import { GlobalService } from '../global-service.service';
import { CustomerService } from '../customer/customer.service';
import { ServicesService } from '../shared/services.service';
import { Customer } from '../customer/customer.component';
import { LoadingService } from '../loading.service';
import * as moment from 'moment';


@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.scss']
})
export class ProductPageComponent implements OnInit {
  productList: Product[] = [];
  menuDto: MenuDto[] = [];
  categoryList: Category[] = [];
  brandList: Brand[] = [];
  cartCount: number = 0;
  alok: Product[] = [];
  

  constructor(private http:Http,private globalService:GlobalService, private backendService: BackendService,private router: Router, private route: ActivatedRoute, public customerService: CustomerService, private persistService: ServicesService,public loadingService: LoadingService) { 
  }
  
    ngOnInit() {

      this.route.params.subscribe(params => {
        // PARAMS CHANGED .. TO SOMETHING REALLY COOL HERE ..
   
        // for example extract the id..

        let id = +params['id'];
        if(params['type']== 'model'){
           // (+) converts string 'id' to a number
          this.getProductByModelId(id);
        }
        else if(params['type']== 'category'){
          this.getProductByCategoryId(id);
        }   
      });
        // console.log(this.backendService.appleInfo);
        this.getMenuDetails();

      
    }

    trackByFn(index, product){
      return index;
    }
    getProductByModelId(id: number){
      //let id = +this.route.snapshot.paramMap.get('id');

      this.loadingService.loading = true;
      console.log("model id", id);
      this.backendService.getProductByModelId(id)
      .subscribe((product) =>{
        //this.productList = product;
        let selectedCustomer:Customer = this.persistService.getCustomerDetailsForSale();

        if(selectedCustomer){

            product.forEach((product)=>{
            if(selectedCustomer.tier == 3){
              product.retail = product.tier3;
            }
            else if(selectedCustomer.tier == 2){
              product.retail = product.tier2;
            }
            else if(selectedCustomer.tier == 1){
              product.retail = product.tier1;
            }
          });
        }
        this.productList = product;
        this.productList = this.productList.slice();

       this.loadingService.loading = false;
      })
    }

    getProductByCategoryId(id: number){
      this.loadingService.loading = true;

      this.backendService.getProductByCategoryId(id)
      .subscribe((product) =>{

        let selectedCustomer:Customer = this.persistService.getCustomerDetailsForSale();

        if(selectedCustomer){

            product.forEach((product)=>{
            if(selectedCustomer.tier == 3){
              product.retail = product.tier3;
            }
            else if(selectedCustomer.tier == 2){
              product.retail = product.tier2;
            }
            else if(selectedCustomer.tier == 1){
              product.retail = product.tier1;
            }
          });
        }
        this.productList = product;
        this.productList = this.productList.slice();
        this.loadingService.loading = false;
      });
    }
  
    getMenuDetails(){

      this.loadingService.loading = true;

  
      this.backendService.getData()
      .subscribe((data)=>{
        //console.log('menu', data);
  
        for(var i =0;i<data.categoryDtoList.length;i++){
          // var str = data.categoryDtoList[i].name;
          //console.log('str', str);
  
          this.categoryList.push(data.categoryDtoList[i]);
        }
        for(var i = 0; i<data.webBrandDtoList.length; i++) {
          var str = data.webBrandDtoList[i].name;
                    // console.log('brand response', data.webBrandDtoList);

          this.brandList.push(data.webBrandDtoList[i]);
        }
        this.brandList = this.brandList.slice();
        console.log('BrandsList', this.brandList);
        this.categoryList = this.categoryList.slice();
        this.loadingService.loading = false;

  });
    }
  
    addProductToCart(product: Product){
      this.loadingService.loading = true;

      let selectedCustomer: Customer = this.persistService.getCustomerDetailsForSale();

     // console.log('product', product);
      product.customerPhoneNo = selectedCustomer.phoneNo;
      product.status = 'Online';
      product.date = moment(Date.now()).format('YYYY-MM-DD HH:mm:ss');

      console.log('Product Quanity to add from ui', product.saleQuantity);

      if(product.saleQuantity > 0){
        this.globalService.addProductToCart(product);
      }
      else {
        alert("Please Enter Valid Quantity");
      }

      this.loadingService.loading = false;

        
      //console.log('added cart product List', this.backendService.productList);
    }

    addAllProducts()
    {
      let productOrderedList: Product[] =[];
      let selectedCustomer: Customer = this.persistService.getCustomerDetailsForSale();

      this.productList.forEach((addedProduct)=>{
        if(addedProduct.saleQuantity > 0) {
          addedProduct.customerPhoneNo = selectedCustomer.phoneNo;
          addedProduct.status = 'Online';
          addedProduct.date = moment(Date.now()).format('YYYY-MM-DD HH:mm:ss');
          productOrderedList.push(addedProduct);
        }
      });
     let rep = this.globalService.addAllProductToCart(productOrderedList);

     console.log('response', rep);
  // Now I need to set sale quantity to 0 so user won't buy same product again.

      // this.productList.forEach((addedProduct)=>{
      //   if(addedProduct.saleQuantity > 0) {
      //     addedProduct.saleQuantity = 0;
      //   }
      // });
    
    
      this.globalService.getPurchasedProductList();



    }

    // getProducts(options: ProductOptions) {
    //   return this.productService.getProducts(options);
    // }
  
  }

  export class Product {
    transactionLineItemId: number;
    productNo: string;
    description: string;
    categoryId: number;
    brandId: number;
    vendorId: number;
    modelId: number;
    cost: number;
    retail: number;
    tier1:number;
    tier2:number;
    tier3:number;
    quantity: number;
    saleQuantity: number;
    ecommerce: boolean;
    tax:boolean
    image: any;
    customerPhoneNo: string;
    date: any;
    status: any;
    featured:boolean;
    onSale: boolean;
    newProduct:boolean;
  }
  
  
  
  export class Category {
    name: string;
    categoryId: number;
  }
  export class Brand {
    brandName: string;
    brandId: number;
    modelDtoList: Model[];
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
  