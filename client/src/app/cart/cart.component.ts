import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global-service.service';
import { Product } from '../product-page/product-page.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  purchasedProductList: Product[] = [];
  totalAmount: number = 0.00;
  totalQuantity: number = 0;

  _subscription: any;
  _totalAmountSubscription: any;
  _totalQuantitySubscription: any;


  constructor(public globalService: GlobalService) { 
    this.ngOnInit();
  }

  ngOnInit() {
    console.log('coming for ngOnit');
    this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();
    console.log('Done for ngOnit');

  }

  trackByIndex(index: number, obj: any): any {
    return index;
  }
  getPurchasedProductList(){

    //this.globalService.getPurchasedProductList();

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

  }

  ngOnDestroy() {
    //prevent memory leak when component destroyed
     this._subscription.unsubscribe();
     this._totalAmountSubscription.unsubscribe();
     this._totalQuantitySubscription.unsubscribe();
  }

}
