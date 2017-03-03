#import "NativeBridge.h"
#include "CommandParser.h"
@implementation NativeBridge
- (NSString *)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken
{
    CommandParser parser;
    std::string result = parser.parse([command cStringUsingEncoding:NSUTF8StringEncoding], [accessToken cStringUsingEncoding:NSUTF8StringEncoding]);
    return [NSString stringWithCString:result.c_str() encoding:[NSString defaultCStringEncoding]];
}
@end
