# Instagram Kit Android

Instagram Kit is an Android library designed to make interacting with Instagram API easier.

## Instagram Kit features
- Log in with Instagram

## Getting started
- visit [Instagram Developer Dashboard](https://www.instagram.com/developer/)
- generate client to access **Client ID**
- under *security* tab, enter *valid redirect uri*, it **has to start with http:// and end with .domainname**
  - for example it can be `http://dummyredirecturi.my`
  
## Usage
### Login
Simply call
```
InstagramManager.authorize(
        activity,
        AuthData("your_client_id", "your_redirect_uri"),
        object : AuthListener {
            override fun onSuccess(accessToken: String?) {
                // handle access token
            }

            override fun onFailure(errorMessage: String?) {
                // handle error message
            }
        })
```
and override activity's `onActivityResult` like this
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == InstagramManager.INSTAGRAM_KIT_AUTH_REQUEST_CODE) {
        InstagramManager.onActivityResult(requestCode, resultCode, data)
    }
}
```
