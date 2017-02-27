#import <Foundation/Foundation.h>
#include "NativeBridge.h"
@interface NativeBridgeObjC : NSObject
- (void)playUri:(NSString *) uri;
- (void)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken;
@end
