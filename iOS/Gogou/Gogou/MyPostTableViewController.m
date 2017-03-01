//
//  MyPostTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/5/11.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MyPostTableViewController.h"
#import "PostTripTableViewController.h"
#import "PostRequirementTableViewController.h"
#import "TripTableViewCell.h"
#import "DemandDetailTableViewController.h"
#import "DemandTableViewCell.h"
#import "TripFilter.h"
#import "Demand.h"
#import "Trip.h"
#import "DemandFilter.h"
#import <RestKit/RestKit.h>
#import "TripDetailTableViewController.h"
#import "GoGouConstant.h"
#import "GGCategory.h"
#import "GGCity.h"
#import "GGTime.h"
#import "DateTranslation.h"
#import "Subscriber.h"
#import "UIView+MJExtension.h"
#import "MJRefresh.h"
#import "MyPostTableViewCell.h"
#import "CacheManager.h"


@interface MyPostTableViewController (){
    NSMutableArray *tripList; //行程列表信息
    NSMutableArray *demandList; //需求列表信息
    TripFilter *tripFilter;
    DemandFilter *demandFilter;
    UISegmentedControl *segmentedControl;
    Subscriber *subscriber;
    NSArray *categoryList;

}
@property (nonatomic) int isTrip;//是否行程标志位
@property (nonatomic) int isRefresh;//刷新标志位


@end

@implementation MyPostTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _isTrip = 0;

    
    tripList = [[NSMutableArray alloc]init];
    demandList = [[NSMutableArray alloc]init];
    
    [self setupNavigationBar];
    
    [self pullUpLoadInit];
    
    [self categoryListRequest:@"zh-s"];
    
    subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
    
    if (_isTrip)
        [self tripSegmentControlSelected];
    else
        [self demandSegmentControlSelected];
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    NSUInteger count = 1;
    
    
    if (_isTrip)
    {    if (tripList)
        count += tripList.count;
    }
    else
    {
        if (demandList)
            count += demandList.count;
    }
    
    return count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0)
        return  44;
    return 60;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0)
        return;
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (_isTrip){
        TripTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        TripDetailTableViewController *vc = [[TripDetailTableViewController alloc]init];
        vc.tripId = cell.Id;
        vc.fromWhere = 1;
        [self.navigationController pushViewController:vc animated:YES];
        
    }
    else{
        DemandTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        DemandDetailTableViewController *vc = [[DemandDetailTableViewController alloc]init];
        vc.demandId = cell.Id;
        vc.fromWhere = 1;
        [self.navigationController pushViewController:vc animated:YES];
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    if (indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:@"MyPostTableViewCell" bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:@"MyPostTableViewCell"];
        MyPostTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:@"MyPostTableViewCell"  forIndexPath:indexPath];
        if (segmentedControl == nil){
            segmentedControl = tmp.segmentController;
            segmentedControl.selectedSegmentIndex = 0;
            segmentedControl.multipleTouchEnabled = NO;
            [segmentedControl addTarget:self action:@selector(segmentControlValueChanged:) forControlEvents:UIControlEventValueChanged];
        }
        cell = tmp;
    }
    else{
        static NSString *tripCellIdentifier = @"TripTableViewCell";
        static NSString *demandCellIdentifier = @"DemandTableViewCell";
        
        if (_isTrip == 1){
            
            UINib *nib = [UINib nibWithNibName:tripCellIdentifier bundle:nil];
            [tableView registerNib:nib forCellReuseIdentifier:tripCellIdentifier];
            //isTripRegNib = YES;
            
            TripTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:tripCellIdentifier];
            
            if (tripList.count != 0){
                Trip *trip = [tripList objectAtIndex:indexPath.row - 1];
                [tmp setupCell:trip];
            }
            else{
                Trip *trip = [Trip new];
                [tmp setupCell:trip];
            }
            
            cell = tmp;
        }
        else{
            
            UINib *nib = [UINib nibWithNibName:demandCellIdentifier bundle:nil];
            [tableView registerNib:nib forCellReuseIdentifier:demandCellIdentifier];
            DemandTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:demandCellIdentifier];
            
            if (demandList.count != 0){
                
                NSString *imagePath;
                Demand *demand = [demandList objectAtIndex:indexPath.row - 1];
                
                 for (GGCategory *item in categoryList){
                     if ([item.name isEqualToString:demand.categoryName]){
                         imagePath = item.imagePath;
                         imagePath = [imagePath stringByAppendingString:@".png"];
                         break;
                     }
                 }
                 
                 [tmp setupCell:demand AndImagePath:imagePath];
                
            }
            else{
                Demand *demand = [Demand new];
                [tmp setupCell:demand AndImagePath:@"shopping"];
            }
            cell = tmp;
        }
    }
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

/*
#pragma mark UITableView + 下拉刷新 默认
- (void)pullDownRefresh
{
    __unsafe_unretained __typeof(self) weakSelf = self;
    
    // 设置回调（一旦进入刷新状态就会调用这个refreshingBlock）
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        [weakSelf loadNewData];
    }];
    
    // 马上进入刷新状态
    [self.tableView.mj_header beginRefreshing];
}
*/
#pragma mark UITableView + 上拉刷新 默认
- (void)pullUpLoadInit
{
    //[self pullDownRefresh];
    
    __unsafe_unretained __typeof(self) weakSelf = self;
    
    // 设置回调（一旦进入刷新状态就会调用这个refreshingBlock）
    self.tableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        [weakSelf loadMoreData];
    }];
    
    //[self.tableView.mj_footer beginRefreshing];
}

#pragma mark - 数据处理相关
#pragma mark 下拉刷新数据
- (void)loadNewData
{
    
    if (_isTrip == 1)
    {
        _isRefresh = 1;
        tripFilter.currentPage = [NSNumber numberWithInteger:1];
        [self tripListRequest:tripFilter];
    }
    else{
        _isRefresh = 1;
        demandFilter.currentPage = [NSNumber numberWithInteger:1];
        [self demandListRequest:demandFilter];
    }
    
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0){
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
    
    
}

- (void) segmentControlValueChanged:(UISegmentedControl*)sender{
    switch (sender.selectedSegmentIndex) {
        case 0:
            sender.selectedSegmentIndex = 0;
            [self demandSegmentControlSelected];
            
            break;
        case 1:
            sender.selectedSegmentIndex = 1;
            [self tripSegmentControlSelected];
        default:
            break;
    }
}

- (void) tripSegmentControlSelected{
    _isTrip = 1;
    
    [self dataListInit];
    [self loadNewData];
     
}

- (void) demandSegmentControlSelected{
    
    _isTrip = 0;
    
    [self dataListInit];
    
    [self loadNewData];
    
}

- (void) dataListInit{
    
    if (_isTrip){
        tripFilter = [TripFilter new];
        tripFilter.currentPage = [NSNumber numberWithInt:1];
        tripFilter.pageSize = [NSNumber numberWithInt:10];
        tripFilter.userName = subscriber.userName;
        tripFilter.departure = @"desc";
        tripFilter.ratingOrder = @"desc";
    }
    else{
        demandFilter = [DemandFilter new];
        demandFilter.currentPage = [NSNumber numberWithInt:1];
        demandFilter.pageSize = [NSNumber numberWithInt:10];
        demandFilter.userName = subscriber.userName;
        demandFilter.serviceFeeOrder = @"desc";
        demandFilter.ratingOrder = @"desc";
    }
    
}


- (void)loadMoreData
{
    if (_isTrip == 1)
    {
        _isRefresh = 0;
        tripFilter.currentPage = [NSNumber numberWithInteger:[tripFilter.currentPage integerValue]+1];
        [self tripListRequest:tripFilter];
    }
    else{
        _isRefresh = 0;
        demandFilter.currentPage = [NSNumber numberWithInteger:[demandFilter.currentPage integerValue]+1];
        [self demandListRequest:demandFilter];
    }
}

/*
 categoryList Request
 */
- (void)categoryListRequest:(NSString *)languageCode{
    
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
    
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
                                                          @"name" : @"name",
                                                          @"imagePath" : @"imagePath"
                                                          //@"languageCode" : @"languageCode",
                                                          //@"image" : @"image",
                                                          //@"fileOutput" : @"fileOutput",
                                                          //@"username" : @"username"
                                                          }];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
    
    NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        categoryList = [result array];
        
        [self.tableView reloadData];
    }
     
                                     failure:^(RKObjectRequestOperation *operation, NSError *error) {
                                         NSLog(@"Failed with error: %@", [error localizedDescription]);
                                     }];
    [operation start];
    
}



/*****
 ***** tripListRequest
 *****/
- (void)tripListRequest:(TripFilter *)tripFilter{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Trip class]];
    
    
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
                                                          @"arrival" : @"arrival",
                                                          @"departure" : @"departure",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"maxheight" : @"maxheight",
                                                          @"maxlength" : @"maxlength",
                                                          @"maxweight" : @"maxweight",
                                                          @"maxwidth" : @"maxwidth",
                                                          @"userName" : @"userName",
                                                          @"description": @"descriptions",
                                                          @"categoryNames" : @"categoryNames",
                                                          @"languageCode" : @"languageCode",
                                                          @"fileOutputs" : @"fileOutputs"}];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:@"/gogou-web/services/trip/list" keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    
    [requestMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"currentPage" : @"currentPage",
                                                         @"pageSize" : @"pageSize",
                                                         @"departure" : @"departure",
                                                         @"ratingOrder" : @"ratingOrder",
                                                         @"categoryName" : @"categoryName",
                                                         @"destination" : @"destination",
                                                         @"origin" : @"origin"
                                                         }];
    
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[TripFilter class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    
    
    // POST to create
    
    [manager postObject:tripFilter path:@"/gogou-web/services/trip/list" parameters:nil
                success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                    
                    NSArray *objects = [mappingResult array];
                    //重新加载数据
                    if (_isRefresh){
                        tripList = [objects mutableCopy];
                    }
                    //下来加载更多数据
                    else{
                        [tripList addObjectsFromArray:objects];
                    }
                    [self.tableView.mj_header endRefreshing];
                    [self.tableView.mj_footer endRefreshing];
                    [self.tableView reloadData];
                }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    
                }
     ];
    
}


/*****
 ***** demandListRequest
 *****/
- (void)demandListRequest:(DemandFilter *)demandFilter{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Demand class]];
    
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
                                                          @"expectTime" : @"expectTime",
                                                          @"productname" : @"productname",
                                                          @"userName" : @"userName",
                                                          @"quantity" : @"quantity",
                                                          @"brand" : @"brand",
                                                          @"serviceFee" : @"serviceFee",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"categoryName" : @"categoryName",
                                                          @"description": @"descriptions",
                                                          @"languageCode" : @"languageCode",
                                                          @"fileOutputs" : @"fileOutputs"}];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:@"/gogou-web/services/demand/list" keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    
    
    [requestMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"currentPage" : @"currentPage",
                                                         @"pageSize" : @"pageSize",
                                                         @"serviceFeeOrder" : @"serviceFeeOrder",
                                                         @"ratingOrder" : @"ratingOrder",
                                                         @"categoryName" : @"categoryName",
                                                         @"origin" : @"origin",
                                                         @"destination" : @"destination"
                                                         }];
    
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[DemandFilter class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    // POST to create
    
    [manager postObject:demandFilter path:@"/gogou-web/services/demand/list" parameters:nil
                success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                    
                    NSArray *objects = [mappingResult array];
                    //重新加载数据
                    if (_isRefresh){
                        demandList = [objects mutableCopy];
                    }
                    //下来加载更多数据
                    else{
                        [demandList addObjectsFromArray:objects];
                    }
                    [self.tableView.mj_header endRefreshing];
                    [self.tableView.mj_footer endRefreshing];
                    [self.tableView reloadData];
                }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    
                }
     ];
    
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Table view delegate

// In a xib-based application, navigation from a table can be handled in -tableView:didSelectRowAtIndexPath:
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here, for example:
    // Create the next view controller.
    <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:<#@"Nib name"#> bundle:nil];
    
    // Pass the selected object to the new view controller.
    
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
