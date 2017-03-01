//
//  RegisterViewController.h
//  Gogou
//
//  Created by xijunli on 16/6/4.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RESTRequestListener.h"

@interface RegisterViewController :UIViewController<UIAlertViewDelegate, RESTRequestListener>{
    int alertClickFlag;
}

@end
