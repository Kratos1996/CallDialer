package com.artixtise.richdialer.data.profile.model

data class UserAccessData(var name: String ="",
                          var gender: String ="",
                          var email: String ="",
                          var userId: String ="",
                          var mobile: String ="",
                          var mobile2: String ="",
                          var address: String ="",
                          var lastUpdatedProfile: Long = 0,
                          var profilePicture:String ="",
                          var instagramUrl:String ="",
                          var linkedInUrl:String ="",
                          var facebookUrl:String ="",
                          var websiteUrl:String ="",
                          var twitterUrl:String ="",
                          var deviceToken:String="",
                          var instagramVisible:Boolean=false,
                          var linkedInVisible:Boolean=false,
                          var facebookVisible:Boolean=false,
                          var websiteVisible:Boolean=false,
                          var twitterVisible:Boolean=false,

                          )

