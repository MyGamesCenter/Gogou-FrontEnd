//
//  CreateOrderTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/4/15.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

#import "CreateOrderTableViewController.h"
#import "CreateOrderTableViewCell0.h"
#import "CreateOrderTableViewCell1.h"
#import "CreateOrderTableViewCell2.h"
#import "CreateOrderTableViewCell3.h"
#import "CreateOrderTableViewCell4.h"
#import "CreateOrderTableViewCell5.h"
#import "CreateOrderTableViewCell6.h"
#import "CreateOrderTableViewCell7.h"
#import "CreateOrderTableViewCell8.h"
#import "CreateOrderTableViewCell9.h"
#import "ImageProcess.h"
#import "Order.h"
#import "GenericResponse.h"
#import "GoGouConstant.h"
#import "OrderDescription.h"
#import <RestKit/RestKit.h>
#import "RESTRequestUtils.h"
#import "UIView+Toast.h"
#import "Subscriber.h"
#import "CacheManager.h"
#import "GGCategory.h"
#import "OrderUtils.h"
#import <CZPicker.h>


@interface CreateOrderTableViewController ()<CZPickerViewDataSource, CZPickerViewDelegate>
{
    NSMutableArray *cellIdentifiers;
    FlatButton *submitButton;
    UIButton *quantityMinusButton;
    UITextField *quantityTextField;
    UITextField *minPriceTextField;
    UITextField *maxPriceTextField;
    UITextField *serviceFeeTextField;
    UITextField *productNameTextField;
    UITextField *productCategoryTextField;
    UIButton *categorySelectedButton;
    UITextField *brandTextField;
    UITextField *telephoneTextField;
    UITextField *usernameTextField;
    UIButton *quantityPlusButton;
    UIButton *serviceFeeMinusButton;
    UIButton *serviceFeePlusButton;
    UITextView *descriptionTextView;
    UITextView *addressTextView;
    Subscriber *subscriber;
    NSString *selectedCategory;
}
@end

@implementation CreateOrderTableViewController

@synthesize categoryList;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    categoryList = [NSMutableArray new];
    
    subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
    
    [self performGetCategoryListRequest:NSLocalizedString(@"language_code", "zh-s") ];
    
    self.navigationItem.title = @"订单详情";
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<10;i++)
    {
        NSString *tmp = @"CreateOrderTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }

    self.tableView.separatorColor = BACKGROUND_CORLOR;
    
    self.tableView.allowsSelection = NO;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 4;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    switch (section) {
        case 0:
            return @"";
            break;
        case 1:
            return @"填写收货地址";
            break;
        case 2:
            return @"订单详情";
            break;
        case 3:
            return @"费用";
            break;
        default:
            break;
    }
    return nil;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSInteger rowCount;
    switch (section) {
        case 0:
            rowCount = 1;
            break;
        case 1:
            rowCount = 3;
            break;
        case 2:
            rowCount = 5;
            break;
        case 3:
            rowCount = 2;
            break;
    
        default:
            break;
    }
    return rowCount;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    
    if (indexPath.section == 0 && indexPath.row == 0)
        height = 70.0f;

    if (indexPath.section == 1 && indexPath.row == 2)
        height = 120.0f;
    if (indexPath.section == 1 && indexPath.row == 2)
        height = 70.0f;
    if (indexPath.section == 2 && indexPath.row == 4)
        height = 120.0f;
    if (indexPath.section == 3 && indexPath.row == 1)
        height = 78.0f;
    
    return height;
}

/* comment out this method to allow
 CZPickerView:titleForRow: to work.
 */
- (NSAttributedString *)czpickerView:(CZPickerView *)pickerView
               attributedTitleForRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    NSAttributedString *att = [[NSAttributedString alloc]
                               initWithString:obj.name
                               attributes:@{
                                            NSFontAttributeName:[UIFont fontWithName:@"Avenir-Light" size:18.0]
                                            }];
    return att;
}

- (NSString *)czpickerView:(CZPickerView *)pickerView
               titleForRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    return obj.name;
}


- (NSInteger)numberOfRowsInPickerView:(CZPickerView *)pickerView{
    return self.categoryList.count;
}

- (void)czpickerView:(CZPickerView *)pickerView didConfirmWithItemAtRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    selectedCategory = obj.name;
    [categorySelectedButton setTitle:obj.name forState:UIControlStateNormal];
    [categorySelectedButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
}

- (IBAction)showWithMultipleSelection:(id)sender {
    CZPickerView *picker = [[CZPickerView alloc] initWithHeaderTitle:NSLocalizedString(@"物品类型", nil) cancelButtonTitle:NSLocalizedString(@"取消", nil) confirmButtonTitle:NSLocalizedString(@"确认", nil)];
    picker.animationDuration = 0.92;
    picker.headerBackgroundColor = BACKGROUND_CORLOR;
    //picker.confirmButtonBackgroundColor = [UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1];
    picker.delegate = self;
    picker.dataSource = self;
    //picker.allowMultipleSelection = YES;
    [picker show];
}

- (void)czpickerViewDidClickCancelButton:(CZPickerView *)pickerView{
    NSLog(@"Canceled.");
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell;
    
    if (indexPath.section == 0 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        CreateOrderTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.CategoryNameValueLabel1.text = @"ALL";
        [tmp.CategoryNameValueLabel2 setHidden:YES];
        [tmp.CategoryNameValueLabel3 setHidden:YES];
        cell = tmp;
    }
   
    if (indexPath.section == 1 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[8] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[8]];
        CreateOrderTableViewCell8 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[8] forIndexPath:indexPath];
        tmp.keyLabel.text = @"收货人";
        tmp.usernameLabel.text = subscriber.userName;
        if (!usernameTextField){
            usernameTextField = tmp.valueTextField;
            usernameTextField.placeholder = @"姓";
        }
        
        cell = tmp;
    }
    if (indexPath.section == 1 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[3] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[3]];
        CreateOrderTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"联系电话";
        
        if (!telephoneTextField){
            telephoneTextField = tmp.ValueTextField;
            telephoneTextField.placeholder = @"请填联系电话";
        }
        
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[5] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[5]];
        CreateOrderTableViewCell5 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[5] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"填写详细收货地址";
        if (!addressTextView)
        {
            addressTextView = tmp.ValueTextView;
            addressTextView.text = @"";
        }
        
        cell = tmp;
    }
    
   
    
    if (indexPath.section == 2 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[3] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[3]];
        CreateOrderTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"物品名称";
        
        if (!productNameTextField){
            productNameTextField = tmp.ValueTextField;
            productNameTextField.placeholder = @"请填写物品名称";
        }
        
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[9] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[9]];
        CreateOrderTableViewCell9 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[9] forIndexPath:indexPath];
        tmp.keyLabel.text = @"物品名称";
        
        if (!categorySelectedButton){
            categorySelectedButton = tmp.valueButton;
            [categorySelectedButton setTitle:NSLocalizedString(@"请选择物品类型", nil) forState:UIControlStateNormal];
            [categorySelectedButton addTarget:self action:@selector(showWithMultipleSelection:) forControlEvents:UIControlEventTouchUpInside];
        }
        
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[4] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[4]];
        CreateOrderTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"数量";
        if (!quantityMinusButton)
        {
            quantityMinusButton = tmp.MinusButton;
            [quantityMinusButton addTarget:self action:@selector(quantityMinusButtonTap:) forControlEvents:UIControlEventTouchUpInside];
        }
        
        if (!quantityPlusButton)
        {
            quantityPlusButton = tmp.PlusButton;
            [quantityPlusButton addTarget:self action:@selector(quantityPlusButtonTap:) forControlEvents:UIControlEventTouchUpInside];
        }
        
        if (!quantityTextField)
        {
            quantityTextField = tmp.ValueTextField;
            [quantityTextField setEnabled:NO];
        }
        
        tmp.UnitLabel.text = @"";
        
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[3] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[3]];
        CreateOrderTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"品牌";
        if (!brandTextField){
            brandTextField = tmp.ValueTextField;
            brandTextField.placeholder = @"请填写品牌";
        }
        
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 4){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[5] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[5]];
        CreateOrderTableViewCell5 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[5] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"备注";
        if (!descriptionTextView)
        {
            descriptionTextView = tmp.ValueTextView;
            descriptionTextView.text = @"";
        }
        
        cell = tmp;
    }
    
    if (indexPath.section == 3 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[6] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[6]];
        CreateOrderTableViewCell6 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[6] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"商品价格";
        if (!minPriceTextField)
        {
            minPriceTextField = tmp.leftTextField;
        }
        
        if (!maxPriceTextField)
        {
            maxPriceTextField = tmp.rightTextField;
        }
        cell = tmp;
    }
    
    if (indexPath.section == 3 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[7] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[7]];
        CreateOrderTableViewCell7 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[7] forIndexPath:indexPath];
        if (!submitButton)
        {
            submitButton = tmp.SubmitButton;
            [submitButton addTarget:self action:@selector(submitButtonTap:) forControlEvents:UIControlEventTouchUpInside];
            [submitButton setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
            [submitButton setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
            [submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            

        }
        
        
        cell = tmp;
        
    }
    
    
    return cell;
}

- (void)submitButtonTap:(id)sender{
    
    
    OrderDescription *orderDescription = [OrderDescription new];
    orderDescription.productName = productNameTextField.text;
    orderDescription.name = subscriber.userName;
    orderDescription.brand = brandTextField.text;
    orderDescription.minPrice = [NSNumber numberWithFloat:[minPriceTextField.text floatValue]];
    orderDescription.maxPrice = [NSNumber numberWithFloat:[maxPriceTextField.text floatValue]];
    orderDescription.quantity = [NSNumber numberWithInt:[quantityTextField.text intValue]];
    orderDescription.categoryName = selectedCategory;
    orderDescription.languageCode = [NSString stringWithFormat:@"%@",@"zh-s"];
    orderDescription.descriptions = descriptionTextView.text;
    orderDescription.origin = _trip.origin;
    
    
    
    Order *order = [Order new];
    order.id = [OrderUtils getOrderNumber];
    order.username = subscriber.userName;
    order.tripId = _trip.Id;
    order.orderStatus = PREORDERED;
    order.serviceFee = [NSNumber numberWithFloat:0.0];
    order.orderDescriptions = [[NSMutableArray alloc]init];
    order.sellerId = _trip.userName;
    
    Address *address = [Address new];
    address.country = addressTextView.text;
    address.telephone = telephoneTextField.text;
    address.userName = usernameTextField.text;

    order.address = address;
    [order.orderDescriptions addObject:orderDescription];
    
    [RESTRequestUtils performCreateOrderRequest:order
                                     subscriber:subscriber
                                       delegate:self];
}

- (void)quantityMinusButtonTap:(id)sender{
    int quantity = [quantityTextField.text intValue];
    if (quantity >= 1)
    {
        quantity--;
        quantityTextField.text = [NSString stringWithFormat:@"%d",quantity];
    }
}

- (void)quantityPlusButtonTap:(id)sender{
    int quantity = [quantityTextField.text intValue];
    quantity++;
    quantityTextField.text = [NSString stringWithFormat:@"%d",quantity];
}

- (void)serviceFeeMinusButtonTap:(id)sender{
    float serviceFee = [serviceFeeTextField.text floatValue];
    if (serviceFee >= 1.0)
    {
        serviceFee -= 1.0;
        serviceFeeTextField.text = [NSString stringWithFormat:@"%f",serviceFee];
    }
}

- (void)serviceFeePlusButtonTap:(id)sender{
    float serviceFee = [serviceFeeTextField.text floatValue];
    if (serviceFee <= 99.0)
    {
        serviceFee += 1.0;
        serviceFeeTextField.text = [NSString stringWithFormat:@"%f",serviceFee];
    }
}

- (void)performGetCategoryListRequest:(NSString *)languageCode
{
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
    [responseMapping addAttributeMappingsFromDictionary:@{@"id"         : @"id",
                                                          @"name"       : @"name",
                                                          @"imagePath"  : @"imagePath",
                                                          @"displayName": @"displayName",
                                                          }];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodAny
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL, @"/gogou-web/services/category/list?languageCode=", languageCode];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
     {
         NSArray *objects = [result array];
         NSLog(@"There are %lu categories", (unsigned long)objects.count);
         [categoryList addObjectsFromArray:objects];
     }
     
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Error during category list request: %@", [error localizedDescription]);
     }];
    [operation start];
}


- (void) onGogouRESTRequestSuccess:(id)result{
    [self.view makeToast:NSLocalizedString(@"下单成功", nil)
                duration:3.0
                position:CSToastPositionBottom
                   title:nil
                   image:nil
                   style:nil
              completion:^(BOOL didTap) {
                  if (didTap)
                  {
                      [self.navigationController popViewControllerAnimated:YES];
                  }
                  else
                  {
                      [self.navigationController popViewControllerAnimated:YES];
                  }
              }];
}

- (void) onGogouRESTRequestFailure{
    [self.view makeToast:NSLocalizedString(@"下单失败", nil)];
}

     
@end
