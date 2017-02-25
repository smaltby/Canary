#import "Hello-Wrapper.h"
#include "hello.hpp"
@implementation Hello_Wrapper
- (void)hello_cpp_wrapped:(NSString *)name {
    Hello hello;
    hello.hello_cpp([name cStringUsingEncoding:NSUTF8StringEncoding]);
}
@end
