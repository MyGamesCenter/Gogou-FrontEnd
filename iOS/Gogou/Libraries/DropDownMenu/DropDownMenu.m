//
//  DropDownMenu.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/24.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DropDownMenu.h"
#import "CreateOrderTableViewController.h"

@implementation DropDownMenu

@synthesize dropDownMenu;
@synthesize dropDownMenuData;
@synthesize oldFrame,newFrame;
@synthesize showDropDownMenu;
@synthesize lineColor,listBgColor;
@synthesize lineWidth;
/*
@synthesize tripId;
@synthesize chatFriendName;
@synthesize chatFriendId;
@synthesize pathFlag;
@synthesize sellerPic;
@synthesize buyerPic;
 */
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/


- (id)initWithFrame:(CGRect)frame {
    
    if(self=[super initWithFrame:frame]){
        //默认的下拉列表中的数据
        dropDownMenuData=[[NSMutableArray alloc]initWithObjects:@"收藏该行程",@"创建订单",nil];

        
        showDropDownMenu=NO; //默认不显示下拉框
        oldFrame=frame; //未下拉时控件初始大小
        //当下拉框显示时，计算出控件的大小。
        newFrame=CGRectMake(frame.origin.x, frame.origin.y, frame.size.width, 30*(dropDownMenuData.count));
        //把背景色设置为透明色，否则会有一个黑色的边
        self.backgroundColor=[UIColor clearColor];
        lineColor=[UIColor lightGrayColor];//默认列表边框线为灰色
        listBgColor=[UIColor lightGrayColor];//默认列表框背景色为白色
        lineWidth=1;     //默认列表边框粗细为1
        [self drawView];//调用方法，绘制控件
    }
    return self;
}

-(void) drawView{
    //这里的坐标是相对于父控件的
    dropDownMenu=[[UITableView alloc]initWithFrame:
              CGRectMake(0,0,
                         oldFrame.size.width,
                         (oldFrame.size.height*(dropDownMenuData.count))-2.0f)];
    dropDownMenu.dataSource=self;
    dropDownMenu.delegate=self;
    dropDownMenu.backgroundColor=listBgColor;
    dropDownMenu.separatorColor=lineColor;
    dropDownMenu.hidden=!showDropDownMenu;//一开始listView是隐藏的，此后根据showList的值显示或隐藏

    [self addSubview:dropDownMenu];
}

-(void)setShow:(BOOL)b{
    showDropDownMenu=b;
    if(showDropDownMenu){
        self.frame=newFrame;
    }else {
        self.frame=oldFrame;
    }
    dropDownMenu.hidden=!b;
}

-(NSInteger)tableView:(UITableView *)table numberOfRowsInSection:(NSInteger)section{
    return dropDownMenuData.count;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellid=@"listviewid";
    UITableViewCell* cell=[tableView dequeueReusableCellWithIdentifier:cellid];
    if(cell==nil){
        cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault
                                    reuseIdentifier:cellid];
    }
    //文本标签
    cell.textLabel.text=(NSString*)[dropDownMenuData objectAtIndex:indexPath.row];
    cell.selectionStyle=UITableViewCellSelectionStyleGray;
    cell.textLabel.font = [UIFont systemFontOfSize:14.0f];
    switch (indexPath.row) {
        case 0:
            cell.imageView.image = [UIImage imageNamed:@"ic_home_18pt"];
            break;
        case 1:
            cell.imageView.image = [UIImage imageNamed:@"ic_attach_money_18pt"];
            break;
        default:
            break;
    }
    return cell;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return oldFrame.size.height;
}

//获得父视图的controller
- (UIViewController *)getViewController
{
    for (UIView* next = [self superview]; next; next = next.superview) {
        UIResponder *nextResponder = [next nextResponder];
        if ([nextResponder isKindOfClass:[UIViewController class]]) {
            return (UIViewController *)nextResponder;
        }
    }
    return nil;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    /*
    [self performSelector:@selector(deselect) withObject:nil afterDelay:0.5f];
    switch (indexPath.row) {
        case 0:{
            [[self getViewController].navigationController popToRootViewControllerAnimated:YES];
        }
            break;
        case 1:{
            CreateOrderTableViewController *createOrderTableViewController = [[CreateOrderTableViewController alloc]init];
            createOrderTableViewController.tripId = tripId;
            createOrderTableViewController.sellerName = chatFriendName;
            createOrderTableViewController.sellerId = chatFriendId;//NSLog(@"聊天界面传给订单界面的sellerid： %@", chatFriendId);
            createOrderTableViewController.sellerPic = sellerPic;
            createOrderTableViewController.buyerPic = buyerPic;
            [[self getViewController].navigationController pushViewController:createOrderTableViewController animated:YES];
        }
            break;
        default:
            break;
    }*/
    [self setShow:NO];
}

- (void)deselect
{
    [dropDownMenu deselectRowAtIndexPath:[dropDownMenu indexPathForSelectedRow] animated:YES];
}

-(void)dropDown{
    if (showDropDownMenu) {
        [self setShow:NO];//消失下拉框
    }else {//如果下拉框尚未显示，则进行显示
        //把dropdownList放到前面，防止下拉框被别的控件遮住
        [self.superview bringSubviewToFront:self];
        [self setShow:YES];//显示下拉框
    }
}

@end
