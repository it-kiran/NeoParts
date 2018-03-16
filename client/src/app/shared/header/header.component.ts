import { Component, OnInit } from '@angular/core';
import { Http,Response } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { BackendService } from '../../services/backend.service';
import { GlobalService } from '../../global-service.service';
import { Product } from '../../product-page/product-page.component';
import { count } from 'rxjs/operator/count';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent  implements OnInit {
list :Product[] = [];
_subscription: any;
_countSubscription: any;
sum =  0;
total: number = 0;

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

this.test();
  // this.globalService.getPurchasedProductList();
  // this.globalService.getPurchasedProductCountForCart();
}

constructor(private http:Http,public backendService:BackendService,private router: Router, public globalService: GlobalService){
this.list = this.globalService.purchasedProductList;

console.log('lit after simple', this.list);

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
        var str = this.samsungData[0].modelDtoList[i].name;
        var note = str.includes("Galaxy Note");
        var s = str.includes("Galaxy S");

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

test(){


//   this.list = this.globalService.purchasedProductList;

  this._subscription =  this.globalService.purchasedProductListChange.subscribe((test)=>{
    this.list = test;
    console.log('subject test', this.list);
  }
);

this._countSubscription = this.globalService.totalPurchasedProductCountChange.subscribe((count)=>{

  this.total = count;
  console.log('subject count', this.total);

});


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
   
 }


}