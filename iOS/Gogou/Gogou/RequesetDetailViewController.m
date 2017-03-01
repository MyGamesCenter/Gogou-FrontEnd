//
//  RequesetDetailViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/12.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "RequesetDetailViewController.h"
#import "DetailCellContent.h"
#import "DetailPageTableViewCell.h"
#import "DetailFirstRowCell.h"
#import "Requirement.h"
#import "FlatButton.h"
#import "ChatViewController.h"

@interface RequesetDetailViewController ()<UITableViewDataSource,UITableViewDelegate>
{
    UITableView *tableView;
    NSMutableArray* content;
    NSMutableArray* contentCell;
}

@end

@implementation RequesetDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    // Do any additional setup after loading the view.
    [self initData];
    self.navigationItem.title = @"需求详情";
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
    tableView.dataSource = self;
    tableView.delegate = self;
    [tableViewFatherView addSubview:tableView];
    
    CGFloat buttonWidth = self.view.frame.size.width * 0.4;
    CGFloat buttonHeight = buttonWidth * 0.3;
    CGFloat buttonX = self.view.center.x - 0.5 * buttonWidth;
    CGFloat buttonY = self.view.frame.size.height - 20.0 - buttonHeight;
    
    FlatButton *button = [[FlatButton alloc]initWithFrame:CGRectMake(buttonX,buttonY,buttonWidth,buttonHeight) type:FBPrimary];
    
    [button addTarget:self action:@selector(buttonClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [button setTitle:@"联系买家" forState:UIControlStateNormal];
    
    [self.view addSubview:button];
}

-(void) buttonClick:(id)sender{
    ChatViewController *cvc = [ChatViewController new];
    [self.navigationController pushViewController:cvc animated:YES];
}

- (void)initData{
     content = [[NSMutableArray alloc]init];
    contentCell = [[NSMutableArray alloc]init];
    DetailCellContent *c2 = [[DetailCellContent alloc]initWithKey:@"类别" andValue:@"Smart phone"];
    DetailCellContent *c3 = [[DetailCellContent alloc]initWithKey:@"品牌" andValue:@"Apple"];
    DetailCellContent *c4 = [[DetailCellContent alloc]initWithKey:@"预期收货时间" andValue:@"2015-1-1"];
    DetailCellContent *c6 = [[DetailCellContent alloc]initWithKey:@"收货地" andValue:@"上海"];
    DetailCellContent *c7 = [[DetailCellContent alloc]initWithKey:@"原产地" andValue:@"加利福尼亚"];
    DetailCellContent *c8 = [[DetailCellContent alloc]initWithKey:@"数量" andValue:@"20"];
    DetailCellContent *c9 = [[DetailCellContent alloc]initWithKey:@"出价" andValue:@"6000美刀"];
    DetailCellContent *c10 = [[DetailCellContent alloc]initWithKey:@"详细描述" andValue:@"whatevedasfnadskjfasdkjhfkasjhfkjashfkjsdhfksjdhfkr"];
    
    [content addObject:c2];
    [content addObject:c3];
    [content addObject:c4];
    [content addObject:c6];
    [content addObject:c7];
    [content addObject:c8];
    [content addObject:c9];
    [content addObject:c10];
    
    
    //test detail first row cell
    DetailFirstRowCell *dfrcell = [[DetailFirstRowCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"UITableViewCellDetailIdentifierKey"];
    [dfrcell setupCellWithContent];
    [contentCell addObject:dfrcell];
    
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
