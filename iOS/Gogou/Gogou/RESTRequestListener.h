//
//  RESTRequestListener.h
//  Gogou
//
//  Created by letian xu on 2016-07-04.
//  Copyright © 2016 GoGou Inc. All rights reserved.
//

/**
 * Listener interface for REST request
 */
@protocol RESTRequestListener <NSObject>

@optional

-(void) onGogouRESTRequestFailure;

-(void) onGogouRESTRequestSuccess:(id)result;

-(void) onGogouOrderUpdateSuccess:(id)result;

-(void) onGogouOrderUpdateFailure;

@end
