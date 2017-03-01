//
//  TripTableViewCell.m
//  Gogou
//
//  Created by xijunli on 16/4/23.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "TripTableViewCell.h"
#import "DateTranslation.h"

@implementation TripTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) setupCell:(Trip *)trip{
    //Trip *trip = [tripList objectAtIndex:indexPath.row];
    self.originLabel.text = trip.origin;
    self.destinationLabel.text = trip.destination;
    
    NSDate *date1 = [DateTranslation DateTranslatedFromString:trip.arrival withLocaleIdentifier:@"zh_CN"];
    self.arrivalLabel.text = [DateTranslation DateFormatTranslated:date1 withLocaleIdentifier:@"zh_CN"];
    
    NSDate *date2 = [DateTranslation DateTranslatedFromString:trip.departure withLocaleIdentifier:@"zh_CN"];
    self.departureLabel.text = [DateTranslation DateFormatTranslated:date2 withLocaleIdentifier:@"zh_CN"];
    
    self.Id = trip.Id;
}

@end
