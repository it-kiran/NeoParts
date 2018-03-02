$(document).ready(function(){

	$('.tp-leftarrow').click(function(){
		var state = 0;
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').each(function(index){
			if($(this).hasClass('active-revslide')){
				state = index - 1;
				if(state == -1)
					state = 4;
			}
		});
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').each(function(index){
			$(this).removeClass('active-revslide');
			$(this).attr('style','width: 100%; height: 100%; overflow: hidden; z-index: 18; visibility: inherit; opacity: 0; background-color: rgba(255, 255, 255, 0);');
			slide_img_opacity_0($(this).children('.slotholder').children('.tp-bgimg'));
		});
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).addClass('active-revslide');
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).attr('style','width: 100%; height: 100%; overflow: hidden; z-index: 20; visibility: inherit; opacity: 1; background-color: rgba(255, 255, 255, 0);');
		slide_img_opacity_1($('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).children('.slotholder').children('.tp-bgimg'));
	});

	$('.tp-rightarrow').click(function(){
		var state = 0;
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').each(function(index){
			if($(this).hasClass('active-revslide')){
				state = index + 1;
				if(state == 5)
					state = 0;
			}
		});
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').each(function(index){
			$(this).removeClass('active-revslide');
			$(this).attr('style','width: 100%; height: 100%; overflow: hidden; z-index: 18; visibility: inherit; opacity: 0; background-color: rgba(255, 255, 255, 0);');
			slide_img_opacity_0($(this).children('.slotholder').children('.tp-bgimg'));

			$(this).children('.tp-parallax-wrap').attr('style', 'width: 100% !important; height: 100% !important; position: absolute; display: block; visibility: hidden; left: 0px; top: 0px; z-index: 60;');
			$(this).children('.tp-parallax-wrap').children().children().children('.tp-caption').attr('style', 'cursor: pointer; width: 100%; height: 100%; z-index: 60; visibility: inherit; text-align: inherit; line-height: 13px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-weight: 400; font-size: 13px; white-space: nowrap; min-height: 0px; min-width: 0px; max-height: none; max-width: none; opacity: 0.0001; transform: translate3d(0px, 0px, 0px); transform-origin: 50% 50% 0px; transition: none 0s ease 0s;');
		});
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).addClass('active-revslide');
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).attr('style','width: 100%; height: 100%; overflow: hidden; z-index: 20; visibility: inherit; opacity: 1; background-color: rgba(255, 255, 255, 0);');
		slide_img_opacity_1($('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).children('.slotholder').children('.tp-bgimg'));
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).children('.tp-parallax-wrap').attr('style','width: 100% !important; height: 100% !important; position: absolute; display: block; visibility: visible; left: 0px; top: 0px; z-index: 60;');
		$('.tp-revslider-mainul').children('.tp-revslider-slidesli').eq(state).children('.tp-parallax-wrap').children().children().children('.tp-caption').attr('style', 'cursor: pointer; width: 100%; height: 100%; z-index: 60; visibility: inherit; text-align: inherit; line-height: 13px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-weight: 400; font-size: 13px; white-space: nowrap; min-height: 0px; min-width: 0px; max-height: none; max-width: none; opacity: 1; transform: translate3d(0px, 0px, 0px); transform-origin: 50% 50% 0px; transition: none 0s ease 0s;');
	});

	slide_time_out();

	function slide_img_opacity_0(dom){
		var style = dom.attr('style');
		style = style.replace(/visibility: inherit;/,'visibility: hidden;');
		style = style.replace(/opacity: 1;/, 'opacity: 0;');
		dom.attr('style', style);
	}
	
	function slide_img_opacity_1(dom){
		var style = dom.attr('style');
		style = style.replace(/visibility: hidden;/,'visibility: inherit;');
		dom.attr('style', style);
		dom.animate({opacity:1},500,function(){});
	}
	
	function slide_time_out(){
		$('.tp-rightarrow').click();
		setTimeout(slide_time_out, 5000);
	}

	var windowScroll_t;
	$(window).scroll(function(){
		clearTimeout(windowScroll_t);
		windowScroll_t=setTimeout(function(){
			if(jQuery(this).scrollTop()>100){
				jQuery('#totop').fadeIn();
			}else{
				jQuery('#totop').fadeOut();
			}
		},500);
	});

	$('#totop').click(function(){
		$('html, body').animate({scrollTop:0},600);
		return false;
	});

	$(function($){
		$(".cms-index-index .footer-container.fixed-position .footer-top,.cms-index-index .footer-container.fixed-position .footer-middle").remove();
	});

	$('.menu-full-width').mouseover(function(){
		$(this).children('.nav-sublist-dropdown').attr('style','display: list-item; list-style: outside none none;');
	});

	$('.menu-full-width').mouseleave(function(){
		$(this).children('.nav-sublist-dropdown').attr('style','display: none; list-style: outside none none;');
	});

	$('.menu-item-has-children').mouseover(function(){
		$(this).children('.nav-sublist-dropdown').attr('style','display: list-item; list-style: outside none none;');
	});

	$('.menu-item-has-children').mouseleave(function(){
		$(this).children('.nav-sublist-dropdown').attr('style','display: none; list-style: outside none none;');
	});

	clock_countdown();
	var time = '2018/03/01 13:00:00';

	function clock_countdown(){
		var date = get_date(time);
		$('#clock').html(date);
		setTimeout(clock_countdown, 1000);
	}

	function get_date(str_date_time){
        var old = new Date(str_date_time);
        var now = new Date();
        var mm;
        var i = old.getTime();
        var ii = now.getTime();
        var iii = Math.ceil((i-ii)/1000);
        var minute = Math.ceil(iii/60);
        if(iii > 60){
        	var minute = Math.ceil(iii/60)-1;
	            iii_extra = iii - minute*60;
	        if(minute > 60){
	            var hour = Math.ceil(minute/60)-1;
	            minute_extra = minute - hour*60;
	            if(hour > 24){
	                var day = Math.ceil(hour/24)-1;
	                hour_extra = hour - day * 24;
	                if(day > 30){
	                    var month = Math.ceil(day/30)-1;
	                    day_extra = day - month*30;
	                    if(day_extra < 10)
	                    	day_extra = '0' + day_extra;
	                    mm = month + ' Months ' + day_extra + ' Days ' + hour_extra + ':' + minute_extra + ':' + iii_extra; 
	                }
	                else{
	                	if(day < 10)
	                    	day = '0' + day;
	                    mm = day + ' Days ' + hour_extra + ':' + minute_extra + ':' + iii_extra;
	                }                
	            }
	            else{
	                mm = hour+ ':' + minute_extra + ':' + iii_extra;
	            }
	        }
	        else {
	            mm = minute + ':' + iii_extra;
	    	}
	    }else{
	    	mm = iii + ' s ';
	    }
        return mm;
    }

	var scrolled=false;
	$(window).scroll(function(){
		if(140<$(window).scrollTop()&&!scrolled){
			if(!$('.header-container .menu-wrapper .mini-cart').length&&!$('.header-container .menu-wrapper .sticky-logo').length){
				$('.header-container').addClass("sticky-header");
				var minicart=$('.header-container .mini-cart').html();
				$('.header-container .menu-wrapper').append('<div class="mini-cart">'+minicart+'</div>');
				var logo_image=$('<div>').append($('.header-container .header > .logo').clone()).html();
				$('.header-container .menu-wrapper').prepend('<div class="sticky-logo">'+logo_image+'</div>');
				$(".sticky-logo img").attr("src","https://www.mobiledefenders.com/skin/frontend/smartwave/porto/images/md-logo-small.png");
				$('.header-container.type15.sticky-header .header > .logo img').addClass("hide");
				$('.header-container.type15.sticky-header .header > .logo img.sticky-logo-image').remove();
				$('.header-container.type15.sticky-header .header > .logo').append('<img src="https://www.mobiledefenders.com/skin/frontend/smartwave/porto/images/md-logo-small.png" class="sticky-logo-image" alt="Sticky Logo"/>');
				$('.header-container .header-wrapper > div').each(function(){
					if($(this).hasClass("container")){
						$(this).addClass("already");
					}else{
						$(this).addClass("container");
					}
				});
				scrolled=true;
			}
			$('.mini-cart').mouseover(function(e){
				$(this).children('.topCartContent').fadeIn(200);
				return false;
			}).mouseleave(function(e){
				$(this).children('.topCartContent').fadeOut(200);
				return false;
			});
		}
		if(140>=$(window).scrollTop()&&scrolled){
			$('.header-container').removeClass("sticky-header");
			$('.header-container .menu-wrapper .mini-cart').remove();
			$('.header-container .menu-wrapper > .sticky-logo').remove();
			$('.header-container.type15 .header > .logo img.sticky-logo-image').remove();
			$('.header-container.type15 .header > .logo img').removeClass("hide");scrolled=false;
			$('.header-container .header-wrapper > div').each(function(){
				if($(this).hasClass("already")){$(this).removeClass("already");
			}else{
				$(this).removeClass("container");
			}
		});
		}
	});


	$('.mini-cart').mouseover(function(e){
		$(this).children('.topCartContent').fadeIn(200);
		return false;
	}).mouseleave(function(e){
		$(this).children('.topCartContent').fadeOut(200);
		return false;
	});
});
