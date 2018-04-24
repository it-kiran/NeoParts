import { Component, OnInit } from '@angular/core';
import { Http,Response } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/map';
import { BackendService } from '../../services/backend.service';
import { GlobalService } from '../../global-service.service';
import { Product } from '../../product-page/product-page.component';
import { count } from 'rxjs/operator/count';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent  implements OnInit {
purchasedProductList :Product[] = [];
_subscription: any;
_countSubscription: any;
_totalAmountSubscription: any;
sum =  0;
totalQuantity: number = 0;
totalAmount: number = 0.00;
productList: Product[] = [];
seletedProductForDelete: Product;

  title = 'app';
searchText: string;

backendData : any=[];

appleData =[];
appleDataIphone :any=[];
appleDataIpad :any=[];

samsungData :any=[];
samsungNote :any=[];
samsungS :any=[];
samsungOther :any=[];

searchData:any=[];
sss=false;
otherData=[];
aaa=[0,1,2,3,4];

noofshow = 5;
values = '';




inputOutsideClicked:boolean;
characters = [
'Finn the human11',
'Finn the human1111',
'Finn the human1111111',
'Jake the dog11',
'Princess bubblegum12',
'Lumpy Space Princess123',
'Beemo1',
'Beemo2'
]

ngOnInit() {

    this.getPurchasedProductList();
}

constructor(private http:Http,public backendService:BackendService,private route: ActivatedRoute, private router: Router, public globalService: GlobalService, private authServcie: AuthService){
//this.list = this.globalService.purchasedProductList;

//console.log('lit after simple', this.list);

   this.backendService.getData()
    .subscribe(data=>{
      console.log(data)
      for(var i =0;i<data.webBrandDtoList.length;i++){
        var str = data.webBrandDtoList[i].brandName;
        var mm = str.includes("APPLE");
        var nm = str.includes("SAMSUNG");

        if(mm){
          this.appleData.push(data.webBrandDtoList[i]);

          this.backendService.appleInfo.push(
            {brandId:this.appleData[0].brandId ,
            brandName:this.appleData[0].brandName
             }
          );
        }
        else if(nm){
          this.samsungData.push(data.webBrandDtoList[i]);}
        else{
          if( data.webBrandDtoList[i].modelDtoList.length){
            if(data.webBrandDtoList[i].modelDtoList.length > this.noofshow){
              var cntiteration = new Array( );
              for(var k = 0 ; k <= Math.floor((data.webBrandDtoList[i].modelDtoList.length - this.noofshow)/this.noofshow); k ++){
                cntiteration[k] = new Array( );
              }
              for(var j = this.noofshow ; j < data.webBrandDtoList[i].modelDtoList.length ; j ++){
                cntiteration[Math.floor((j-this.noofshow)/5)].push(j);
              }
              data.webBrandDtoList[i]['cntiteration'] = cntiteration;
            }
            this.otherData.push(data.webBrandDtoList[i]);
          }
        }
      }
      // console.log(this.otherData)
      for(var i = 0;i<this.appleData[0].modelDtoList.length;i++){
        var str = this.appleData[0].modelDtoList[i];
        var m = str.name.includes("iPhone");
        var n = str.name.includes("iPad");

        if(m){
          this.appleDataIphone.push(str)
          // console.log(this.appleDataIphone);
        }
        if(n){
          this.appleDataIpad.push(str)
        }
        else{}
      }

      for(var i = 0;i<this.samsungData[0].modelDtoList.length;i++){
        var str = this.samsungData[0].modelDtoList[i];
        var note = str.name.includes("Galaxy Note");
        var s = str.name.includes("Galaxy S");

        if(note){
          this.samsungNote.push(str)
        }
        else if(s){
          this.samsungS.push(str)
        }
        else{
          this.samsungOther.push(str)
        }
      }



    });



}

testLogin(){
  
  this.authServcie.login();

}

goToViewCartPage(){

  console.log("OKEY ROUTER");
  this.router.navigate(['/viewCart']);
}
 getPurchasedProductList(){

  // this._subscription =  this.globalService.purchasedProductListChange.subscribe((products)=>{
  //   this.purchasedProductList = products;
  //   console.log('Product on change by subject', this.purchasedProductList);
  // });
  
  this._subscription =  this.globalService.purchasedProductListChange.subscribe((products)=>{
    this.purchasedProductList = products;
    console.log('Product on change by subject From Cart page', this.purchasedProductList);
  });

this._countSubscription = this.globalService.totalPurchasedProductCountChange.subscribe((count)=>{
  this.totalQuantity = count;
  console.log('subject count', this.totalQuantity);
});

this._totalAmountSubscription = this.globalService.totalPurchasedProductAmountChange.subscribe((totalAmount)=>{
  this.totalAmount = totalAmount;
  console.log('subject Total Amount', this.totalAmount);
});


}

setProductToDeleteFromCart(product: Product){
this.seletedProductForDelete = product;
}
deleteProduct(){
  this.globalService.deleteProductFromCart(this.seletedProductForDelete);
}


onKey(event: any) { // without type info
  this.values='';
  this.values = event.target.value ;
  this.backendService.getFilterSearch(this.values)
  .subscribe(data=>{
    for(var i = 0; i <data.length;i++){
      var str = data[i].description;
        var mm = str.includes(this.values);
        if(mm){
          this.searchData.push(
            data[i]
          )}
    }
    // console.log(this.searchData)
  })
}

onClickedOutside(e: Event) {
  if(e.isTrusted){
    this.inputOutsideClicked = true;
  }
  else{
    this.inputOutsideClicked = false;
  }
  // console.log('Clicked outside:', e);
}
ngOnDestroy() {
  //prevent memory leak when component destroyed
   this._subscription.unsubscribe();
   this._countSubscription.unsubscribe();
   this._totalAmountSubscription.unsubscribe();
   
 }


}