#ifndef SPOTIFY_PLUSPLUS_USER_H
#define SPOTIFY_PLUSPLUS_USER_H

#include <string>
#include <vector>
#include <map>
#include <memory>
#include "Followers.h"
#include "Image.h"
#include "UserPublic.h"
#include "utils/json.h"

class User : public UserPublic
{
public:
    User(nlohmann::json userJson);

    std::string GetBirthdate();
    std::string GetCountry();
    std::string GetEmail();
    std::string GetProduct();

private:
    std::string birthdate;
    std::string country;
    std::string email;
    std::string product;
};


#endif
