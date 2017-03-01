//
//  MyOrderViewController.m
//  Gogou
//
//  Created by xijunli on 16/9/14.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MyOrderViewController.h"
#import "MyOrderTableViewCell1.h"
#import "MyOrderTableViewCell2.h"
#import "OrderFilter.h"
#import "OrderStatus.h"
#import <RestKit/RestKit.h>
#import "Order.h"
#import "GoGouConstant.h"
#import "Subscriber.h"
#import "CacheManager.h"
#import "RESTRequestUtils.h"
#import "OrderFilter.h"
#import "OrderDescription.h"
#import "GenericResponse.h"
#import "UIView+Toast.h"
#import "OrderDetailTableViewController.h"

@interface MyOrderViewController (){
    OrderStatus orderStatusSelected;
    NSMutableArray *orderList;
    NSMutableArray *categoryList;
    Subscriber *subscriber;
    OrderFilter *orderFilter;
}
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end

@implementation MyOrderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    categoryList = [NSMutableArray new];
    [self performGetCategoryListRequest:NSLocalizedString(@"language_code", "zh-s")];
    orderList = [NSMutableArray new];
    subscriber = [[CacheManager sharedManager]readSubscriberInfoFromLocalStorage];
    [self setupNavigationBar];
    [self segmentedControlInit];
    [self tableViewInit];

}

- (void) segmentedControlInit{
    _segmentedControl.multipleTouchEnabled = NO;
    _segmentedControl.selectedSegmentIndex = 0;
    orderStatusSelected = 0;
    [_segmentedControl addTarget:self action:@selector(segmentControlValueChanged:) forControlEvents:UIControlEventValueChanged];
    
    orderStatusSelected = PREORDERED;
    orderFilter = [OrderFilter new];
    orderFilter.currentPage = [NSNumber numberWithInteger:1];
    orderFilter.pageSize = [NSNumber numberWithInteger:10];
    orderFilter.username = subscriber.userName;
    orderFilter.orderStatus = orderStatusSelected;
    [RESTRequestUtils performGetOrderListRequest:orderFilter
                                      subscriber:subscriber
                                        delegate:self];
    
}

- (void) tableViewInit{
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.backgroundColor = [UIColor lightTextColor];
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
}


- (void)segmentControlValueChanged:(UISegmentedControl*)sender{
    switch (sender.selectedSegmentIndex) {
        case 0:
        {
           
            orderFilter = [OrderFilter new];
            orderFilter.currentPage = [NSNumber numberWithInteger:1];
            orderFilter.pageSize = [NSNumber numberWithInteger:10];
            orderFilter.username = subscriber.userName;
            [RESTRequestUtils performGetOrderListRequest:orderFilter
                                              subscriber:subscriber
                                                delegate:self];
        }
            break;
        case 1:
        {
            orderStatusSelected = CONFIRMED;
            orderFilter = [OrderFilter new];
            orderFilter.currentPage = [NSNumber numberWithInteger:1];
            orderFilter.pageSize = [NSNumber numberWithInteger:10];
            orderFilter.username = subscriber.userName;
            orderFilter.orderStatus = orderStatusSelected;
            [RESTRequestUtils performGetOrderListRequest:orderFilter
                                              subscriber:subscriber
                                                delegate:self];
        }
            break;
        case 2:
        {
            orderStatusSelected = DELIVERED;
            orderFilter = [OrderFilter new];
            orderFilter.currentPage = [NSNumber numberWithInteger:1];
            orderFilter.pageSize = [NSNumber numberWithInteger:10];
            orderFilter.username = subscriber.userName;
            orderFilter.orderStatus = orderStatusSelected;
            [RESTRequestUtils performGetOrderListRequest:orderFilter
                                              subscriber:subscriber
                                                delegate:self];
        }
            break;
        case 3:
        {
            orderStatusSelected = COLLECTED;
            orderFilter = [OrderFilter new];
            orderFilter.currentPage = [NSNumber numberWithInteger:1];
            orderFilter.pageSize = [NSNumber numberWithInteger:10];
            orderFilter.username = subscriber.userName;
            orderFilter.orderStatus = orderStatusSelected;
            [RESTRequestUtils performGetOrderListRequest:orderFilter
                                              subscriber:subscriber
                                                delegate:self];
        }
            break;
            
        default:
            break;
    }
    
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return orderList.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    
    
    UINib *nib = [UINib nibWithNibName:@"MyOrderTableViewCell2" bundle:nil];
    [tableView registerNib:nib forCellReuseIdentifier:@"MyOrderTableViewCell2"];
    MyOrderTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:@"MyOrderTableViewCell2"  forIndexPath:indexPath];
    
    if (orderList.count != 0){
        
        NSString *imagePath;
        Order *order = [orderList objectAtIndex:indexPath.section];
        OrderDescription *orderDescription = order.orderDescriptions[0];
        for (GGCategory *item in categoryList){
            if ([item.name isEqualToString:orderDescription.categoryName]){
                imagePath = item.imagePath;
                imagePath = [imagePath stringByAppendingString:@".png"];
                break;
            }
        }
        
        [tmp setupCellWith:order AndImagePath:imagePath AndIsDemander:YES];
        tmp.bottomButton1.tag = indexPath.row;
        tmp.bottomButton2.tag = indexPath.row;
        [tmp.bottomButton1 addTarget:self action:@selector(leftButtonTap:) forControlEvents:UIControlEventTouchUpInside];
        [tmp.bottomButton2 addTarget:self action:@selector(rightButtonTap:) forControlEvents:UIControlEventTouchUpInside];
    }
    
    cell = tmp;
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    OrderDetailTableViewController *orderDetailTableViewController = [OrderDetailTableViewController new];
    orderDetailTableViewController.order = orderList[indexPath.row];
    
    [self.navigationController pushViewController:orderDetailTableViewController animated:YES];
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if ([cell respondsToSelector:@selector(setSeparatorInset:)])
    {
        [cell setSeparatorInset:UIEdgeInsetsZero];
    }
    if ([cell respondsToSelector:@selector(setPreservesSuperviewLayoutMargins:)])
    {
        [cell setPreservesSuperviewLayoutMargins:NO];
    }
    if ([cell respondsToSelector:@selector(setLayoutMargins:)])
    {
        [cell setLayoutMargins:UIEdgeInsetsZero];
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (section == 0)
        return 0;
    else
        return 23.0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 239;
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


- (void)leftButtonTap:(id)Sender{
    
}

- (void)rightButtonTap:(UIButton *)Sender{
    Order *order = [orderList objectAtIndex:Sender.tag];
    order.orderStatus +=1;
    [RESTRequestUtils performUpdateOrderRequest:order subscriber:subscriber delegate:self];
    
}

- (void) onGogouOrderUpdateSuccess:(id)result{
    [self.view makeToast:NSLocalizedString(@"订单更新成功", nil)];
    
}

- (void) onGogouOrderUpdateFailure{
    [self.view makeToast:NSLocalizedString(@"订单更新失败", nil)];
    [RESTRequestUtils performGetOrderListRequest:orderFilter subscriber:subscriber delegate:self];

}

- (void) onGogouRESTRequestSuccess:(id)result{
    orderList = [NSMutableArray new];
    [orderList addObjectsFromArray:result];
    [self.tableView reloadData];
}

- (void) onGogouRESTRequestFailure{
    
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
