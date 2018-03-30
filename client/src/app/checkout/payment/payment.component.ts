import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../../global-service.service';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../../product-page/product-page.component';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  purchasedProductList: Product[] = [];
  totalAmount: number = 0.00;
  totalQuantity: number = 0;

  _subscription: any;
  _totalAmountSubscription: any;
  _totalQuantitySubscription: any;
  constructor(private globalService: GlobalService,private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router) {
    this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();
   }

  ngOnInit() {
    this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();
  }

  getPurchasedProductList(){

    this.globalService.getPurchasedProductList();

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

  goToCheckoutPage(){
    this.router.navigate(['/checkout']);
  }
  goShippingPage(){

    this.router.navigate(['/shipping']);
   }

   ngOnDestroy() {
    //prevent memory leak when component destroyed
     this._subscription.unsubscribe();
     this._totalAmountSubscription.unsubscribe();
     this._totalQuantitySubscription.unsubscribe();
  }
  

}
