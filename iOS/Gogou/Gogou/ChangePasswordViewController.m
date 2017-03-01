//
//  ChangePasswordViewController.m
//  Gogou
//
//  Created by xijunli on 16/6/28.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "ChangePasswordViewController.h"
#import "FlatButton.h"
#import "ImageProcess.h"
#import "ChangePasswordTableViewCell.h"
#import "Subscriber.h"
#import <RestKit/RestKit.h>
#import "RESTRequestUtils.h"
#import "DataProcess.h"
#import "CacheManager.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface ChangePasswordViewController ()<UITableViewDelegate,UITableViewDataSource>{
    
    
    
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet FlatButton *button;
@property (nonatomic) Subscriber* subscriber;//登录者信息
@property (nonatomic,weak) UITextField *originalPasswordTextField;
@property (nonatomic,weak) UITextField *newpasswordTextField;
@property (nonatomic,weak) UITextField *confirmTextField;
@end

@implementation ChangePasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self dataInit];
    [self tableviewInit];
    [self setupNavigationBar];
    [self buttonInit];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dataInit{
    _subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
}

- (void)tableviewInit{
    
    _tableView.separatorColor = BACKGROUND_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = NSLocalizedString(@"修改登录密码", nil);
}

- (void)buttonInit{
    
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    [_button setTitle:NSLocalizedString(@"确认提交", nil) forState:UIControlStateNormal];
    [_button addTarget:self action:@selector(buttonTap:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)buttonTap:(id) sender{
    if ([self checkOriginalPassword:_subscriber] && [self checkNewPasswordConfirmation]){
        _subscriber.clearPassword = _confirmTextField.text;
        [RESTRequestUtils performUpdateSubsriberRequest:_subscriber delegate:self];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    
    static NSString *cellIdentifier = @"ChangePasswordTableViewCell";
    
    if (indexPath.row == 0)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        ChangePasswordTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (_originalPasswordTextField == nil){
            tmp.keyLabel.text = NSLocalizedString(@"原密码", nil);
            _originalPasswordTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = @"原密码";
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 1)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        ChangePasswordTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (_newpasswordTextField == nil){
            tmp.keyLabel.text = NSLocalizedString(@"新密码", nil);
            _newpasswordTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"新密码", nil);
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 2)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        ChangePasswordTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (_confirmTextField == nil){
            tmp.keyLabel.text = NSLocalizedString(@"确认新密码", nil);
            _confirmTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"确认新密码", nil);
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

/**
 *检查原密码是否正确
 * @param subscriber
 */
-(BOOL) checkOriginalPassword:(Subscriber *)subscriber{
    // xijun to do
    
    NSString *clearPassword = [DataProcess decipherWithString:subscriber.encodedPassword];
    if ([clearPassword isEqualToString:_newpasswordTextField.text])
        return true;
    else
        return false;
    
    return true;
}

/**
 *检查新密码输入两次是否一致
 */
-(BOOL) checkNewPasswordConfirmation{
    if ([_newpasswordTextField.text isEqualToString:_confirmTextField.text])
        return true;
    else
        return false;
    //正则表达式检查 xijun to do
}

#pragma mark - RESTRequestListener
- (void)onGogouRESTRequestSuccess:(id)result
{
    //alertClickFlag = true;
    
    UIAlertView *mBoxView =[[UIAlertView alloc]
                            initWithTitle:NSLocalizedString(@"修改密码成功！", nil)
                            message:nil
                            delegate:self
                            cancelButtonTitle:nil
                            otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onGogouRESTRequestFailure
{
    //alertClickFlag = false;
    UIAlertView *mBoxView = [[UIAlertView alloc]
                             initWithTitle:NSLocalizedString(@"修改密码失败！", nil)
                             message:nil
                             delegate:self
                             cancelButtonTitle:nil
                             otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
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
