//
//  AddAddressViewController.m
//  Gogou
//
//  Created by xijunli on 16/5/27.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "AddAddressViewController.h"
#import "FlatButton.h"
#import "ImageProcess.h"
#import "AddAddressTableViewCell1.h"
#import "AddAddressTableViewCell2.h"
#import "AddAddressTableViewCell3.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface AddAddressViewController ()<UITableViewDelegate,UITableViewDataSource>
{
    UITextField *firstNameTextField;
    UITextField *lastNameTextField;
    UITextField *phoneNumberTextField;
    UITextView *addressTextView;
    NSMutableArray *cellIdentifiers;
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet FlatButton *button;


@end

@implementation AddAddressViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self tableviewInit];
    [self buttonInit];
    [self setupNavigationBar];
    [self cellIdentifiersInit];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)tableviewInit{
    
    _tableView.separatorColor = BACKGROUND_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
}

- (void) cellIdentifiersInit{
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=1;i<4;i++)
    {
        NSString *tmp = @"AddAddressTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
    
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = NSLocalizedString(@"新建地址", nil);
}

- (void)buttonInit{
    //[_exitButton.layer setBorderColor:(__bridge CGColorRef _Nullable)([UIColor grayColor])];
    //[_exitButton.layer setBorderWidth:1.0];
    /*
     _exitButton.layer.borderColor = [UIColor grayColor].CGColor;
     _exitButton.layer.borderWidth = 0.5;
     _exitButton.layer.cornerRadius = 0.3;
     _exitButton.tintColor = BACKGROUND_CORLOR;
     */
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    [_button setTitle:NSLocalizedString(@"确认提交", nil) forState:UIControlStateNormal];
}


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 2)
        return 135;
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    return NSLocalizedString(@"填写收货地址", nil);
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    
    
    if (indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[0] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[0]];
        
        AddAddressTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"收货人", nil);
        if (firstNameTextField == nil){
            firstNameTextField = tmp.valueTextField1;
            tmp.valueTextField1.placeholder = NSLocalizedString(@"姓", nil);
        }
        if (lastNameTextField == nil){
            lastNameTextField = tmp.valueTextField2;
            tmp.valueTextField2.placeholder = NSLocalizedString(@"名", nil);
        }
        cell = tmp;
    }
    
    if (indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        
        AddAddressTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"联系电话", nil);
        if (phoneNumberTextField == nil){
            phoneNumberTextField = tmp.valueTextField;
        }
        cell = tmp;
    }
    
    if (indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        
        AddAddressTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"详细收货地址", nil);
        if (addressTextView == nil){
            addressTextView = tmp.valueTextView;
        }
        cell = tmp;
    }
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
