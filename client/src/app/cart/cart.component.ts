import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global-service.service';
import { Product } from '../product-page/product-page.component';
import { CustomerService } from '../customer/customer.service';
import { ServicesService } from '../shared/services.service';
import { Customer } from '../customer/customer.component';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  purchasedProductList: Product[] = [];
  totalAmount: number = 0.00;
  totalQuantity: number = 0;
  seletedProductForDelete: Product;


  _subscription: any;
  _totalAmountSubscription: any;
  _totalQuantitySubscription: any;


  constructor(public globalService: GlobalService, private customerService: CustomerService, private persistService: ServicesService) { 
    // this.ngOnInit();
    this.getPurchasedProductList();

  }

  ngOnInit() {
    // this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();
  }

  trackByIndex(index: number, obj: any): any {
    return index;
  }
  getPurchasedProductList(){

    // let token = this.customerService.getToken();
    // let selectedCustomer:Customer = this.persistService.getCustomerDetailsForSale();
    // if(token && selectedCustomer){
      this.globalService.getPurchasedProductList();
    // }

    this._subscription =  this.globalService.purchasedProductListChange.subscribe((products)=>{
      this.purchasedProductList = products;
      console.log('Product on change by subject From Cart page', this.purchasedProductList);
    });
    this._totalAmountSubscription = this.globalService.totalPurchasedProductAmountChange.subscribe((totalAmount)=>{
      this.totalAmount = totalAmount;
      console.log('subject Total Amount', this.totalAmount);
    });

    this._totalQuantitySubscription = this.globalService.totalPurchasedProductCountChange.subscribe((count)=>{
      this.totalQuantity = count;
      console.log('subject count', this.totalQuantity);
    });

    console.log('purchaded product list at end', this.purchasedProductList);
  }

  updateProductFromCart(product: Product){
    this.globalService.updateProductFromCart(product);
  }

  setProductToDeleteFromCart(product: Product){
    this.seletedProductForDelete = product;
    console.log('selected product for delete', this.seletedProductForDelete);
    }

    deleteProduct(){
      console.log('final delete', this.seletedProductForDelete);
      this.globalService.deleteProductFromCart(this.seletedProductForDelete);
    }

    clearShopingCart(){
      let customer: Customer = this.persistService.getCustomerDetailsForSale();
      // TODO Need to fix this from customer to email address.
      this.globalService.clearCart(customer.phoneNo);
    }

  ngOnDestroy() {
    //prevent memory leak when component destroyed
     this._subscription.unsubscribe();
     this._totalAmountSubscription.unsubscribe();
     this._totalQuantitySubscription.unsubscribe();
  }

}
