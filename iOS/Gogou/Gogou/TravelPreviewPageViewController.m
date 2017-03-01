//
//  TravelPreviewPageViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/13.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "TravelPreviewPageViewController.h"
#import "DetailCellContent.h"
#import "DetailPageTableViewCell.h"
#import "DetailFirstRowCell.h"
#import "Trip.h"
#import "FlatButton.h"

@interface TravelPreviewPageViewController ()<UITableViewDelegate,UITableViewDataSource>{
    UITableView *tableView;
    NSMutableArray* content;
    NSMutableArray* contentCell;
}


@end

@implementation TravelPreviewPageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    self.view.backgroundColor = [UIColor whiteColor];
    [self initData];
    self.navigationItem.title = @"行程发布预览";
    CGRect rootViewRect = self.view.frame;
    CGFloat navigationBarMaxY = CGRectGetMaxY(self.navigationController.navigationBar.frame);
    CGFloat tableViewFatherViewWidth = rootViewRect.size.width;
    CGFloat tableViewFatherViewHeight = (rootViewRect.size.height - navigationBarMaxY) * 0.8;
    //创建tableview的父视图
    UIView *tableViewFatherView = [[UIView alloc]initWithFrame:CGRectMake(0, navigationBarMaxY, tableViewFatherViewWidth, tableViewFatherViewHeight)];
    //设置tableviewFatherView视图原点的位置
    [tableViewFatherView setBounds:CGRectMake(0, navigationBarMaxY, tableViewFatherViewWidth, tableViewFatherViewHeight)];
    
    [self.view addSubview:tableViewFatherView];
    
    CGFloat tableViewWidth = tableViewFatherView.frame.size.width;
    CGFloat tableViewHeight = tableViewFatherViewHeight ;
    //这里tableView的原点位置（0，0）是相对于其父视图的位置
    CGRect tableViewRect = CGRectMake(0, 0, tableViewWidth, tableViewHeight);
    tableView = [[UITableView alloc]initWithFrame:tableViewRect style:UITableViewStylePlain];
    /*NSLog(@"%f,%f",self.view.frame.size.height,self.view.frame.size.width);
     CGSize contentSize = CGSizeMake(self.view.frame.size.width, tableViewHeight);
     [tableView setContentSize:contentSize];
     NSLog(@"%f,%f",tableView.contentSize.height,tableView.contentSize.width);*/
    tableView.dataSource = self;
    tableView.delegate = self;
    [tableViewFatherView addSubview:tableView];
    
    CGFloat buttonWidth = self.view.frame.size.width * 0.4;
    CGFloat buttonHeight = buttonWidth * 0.3;
    CGFloat buttonX = self.view.center.x - 0.5 * buttonWidth;
    CGFloat buttonY = self.view.frame.size.height - 20.0 - buttonHeight;
    
    FlatButton *button = [[FlatButton alloc]initWithFrame:CGRectMake(buttonX,buttonY,buttonWidth,buttonHeight) type:FBPrimary];
    
    [button setTitle:@"确认发布" forState:UIControlStateNormal];
    
    [self.view addSubview:button];
}

- (void)initData{
    //DetailCellContent *c0 = [[DetailCellContent alloc]initWithKey:[UIImage imageNamed:@"7C92C898-4DEA-49CD-8B96-0215554A17D2.png"] andValue:[UIImage imageNamed:@"ic_insert_emoticon.png"]];
    content = [[NSMutableArray alloc]init];
    contentCell = [[NSMutableArray alloc]init];
    DetailCellContent *c2 = [[DetailCellContent alloc]initWithKey:@"出发地" andValue:_trip.departureName];
    DetailCellContent *c3 = [[DetailCellContent alloc]initWithKey:@"目的地" andValue:_trip.destinationName];
    DetailCellContent *c4 = [[DetailCellContent alloc]initWithKey:@"出发时间" andValue:@"2015-1-1"];
    DetailCellContent *c5 = [[DetailCellContent alloc]initWithKey:@"到达时间" andValue:@"2015-1-1"];
    DetailCellContent *c6 = [[DetailCellContent alloc]initWithKey:@"服务费" andValue:@"12 dollar"];
    DetailCellContent *c7 = [[DetailCellContent alloc]initWithKey:@"优先携带物品" andValue:@"beer"];
    DetailCellContent *c8 = [[DetailCellContent alloc]initWithKey:@"最大重量" andValue:@"20kg"];
    DetailCellContent *c9 = [[DetailCellContent alloc]initWithKey:@"最大体积" andValue:@"2m3"];
    DetailCellContent *c10 = [[DetailCellContent alloc]initWithKey:@"详细描述" andValue:_trip.detail];
    
    //[content addObject:c0];
    [content addObject:c2];
    [content addObject:c3];
    [content addObject:c4];
    [content addObject:c5];
    [content addObject:c6];
    [content addObject:c7];
    [content addObject:c8];
    [content addObject:c9];
    [content addObject:c10];
    
    
    
    static NSString *cellIdentifier=@"UITableViewCellDetailIdentifierKey";
    
    for (DetailCellContent *item in content){
        DetailPageTableViewCell* cell = [[DetailPageTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        [cell setCellContentWithInfo:item];
        [contentCell addObject:cell];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - 数据源方法
#pragma mark 返回分组数
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

#pragma mark 返回每组行数
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return contentCell.count;
}

#pragma mark返回每行的单元格
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return contentCell[indexPath.row];
}

#pragma mark - 代理方法
#pragma mark 重新设置单元格高度
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    DetailPageTableViewCell *cell= contentCell[indexPath.row];
    return cell.height;
}

#pragma mark 重写状态样式方法
-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
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
