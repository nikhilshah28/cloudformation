{
	"AWSTemplateFormatVersion": "2010-09-09",
	"Description": "Create VPC for VDI Solution",
	"Parameters": {
		"AZ1": {
			"Description":"Primary Availability Zone",
			"Type": "String",
			"Default": "us-east-1b"
		},
		"AZ2": {
			"Description":"Secondary Availability Zone",
                        "Type": "String",
                        "Default": "us-east-1c"
                }
	},
	"Resources": {
		"VPC": {
			"Type" : "AWS::EC2::VPC",
			"Properties" : {
				"CidrBlock" : "10.0.0.0/16",
				"EnableDnsSupport" : "false",
				"EnableDnsHostnames" : "true",
				"InstanceTenancy" : "default",
				"Tags" : [ {"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} } ]
			}
		},
		"InternetGateway": {
			"Type" : "AWS::EC2::InternetGateway",
			"Properties" : {
        		"Tags" : [ {"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} } ]
			}
		},
		"AttachGateway": {
			"Type" : "AWS::EC2::VPCGatewayAttachment",
			"Properties" : {
				"VpcId" : { "Ref" : "VPC" },
				"InternetGatewayId" : { "Ref" : "InternetGateway" }
			}
		},
		"EIP" : {
      			"Type" : "AWS::EC2::EIP",
      			"Properties" : {
        			"Domain" : "VPC"
      			}
    		},
		"PrivateSubnet": {
			"Type" : "AWS::EC2::Subnet",
			"Properties" : {
				"VpcId" : { "Ref" : "VPC" },
				"CidrBlock" : "10.0.1.0/24",
				"AvailabilityZone" : { "Ref" : "AZ1" },
				"Tags" : [
					{"Key" : "Name", "Value" : "Private Subnet" },
					{"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} }
				]
			}
		},
		"PrivateSubnet2": {
                        "Type" : "AWS::EC2::Subnet",
                        "Properties" : {
                                "VpcId" : { "Ref" : "VPC" },
                                "CidrBlock" : "10.0.2.0/24",
                                "AvailabilityZone" : { "Ref" : "AZ2" },
                                "Tags" : [
                                        {"Key" : "Name", "Value" : "Private Subnet 02" },
                                        {"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} }
                                ]
                        }
                },
		"PublicSubnet": {
			"Type" : "AWS::EC2::Subnet",
			"Properties" : {
				"VpcId" : { "Ref" : "VPC" },
				"CidrBlock" : "10.0.5.0/24",
				"MapPublicIpOnLaunch": "True",
				"Tags" : [
					{"Key" : "Name", "Value" : "Public Subnet" },
					{"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} }
				]
			}
		},
		"NATGateway" : {
                        "Type" : "AWS::EC2::NatGateway",
                        "Properties" : {
                                "AllocationId" : { "Fn::GetAtt" : ["EIP", "AllocationId"]},
                                "SubnetId" : { "Ref" : "PublicSubnet" }
                        }
                },
		"PublicRouteTable": {
			"Type" : "AWS::EC2::RouteTable",
			"Properties" : {
				"VpcId" : {"Ref" : "VPC"},
				"Tags" : [
					{"Key" : "Name", "Value" : "Public Route Table" },
					{"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} }
				]
			}
		},
		"PublicRouteTableEntry": {
			"Type" : "AWS::EC2::Route",
			"DependsOn" : "AttachGateway",
			"Properties" : {
				"RouteTableId" : { "Ref" : "PublicRouteTable" },
				"DestinationCidrBlock" : "0.0.0.0/0",
				"GatewayId" : { "Ref" : "InternetGateway" }
			}
		},
		"PublicRouteTableSubnetAssociation": {
			"Type" : "AWS::EC2::SubnetRouteTableAssociation",
			"Properties" : {
				"SubnetId" : { "Ref" : "PublicSubnet" },
				"RouteTableId" : { "Ref" : "PublicRouteTable" }
			}
		},
		"PrivateRouteTable": {
			"Type" : "AWS::EC2::RouteTable",
			"Properties" : {
				"VpcId" : {"Ref" : "VPC"},
				"Tags" : [
					{"Key" : "Name", "Value" : "Private Route Table" },
					{"Key" : "Application", "Value" : { "Ref" : "AWS::StackId"} }
				]
			}
		},
		"PrivateRouteTableEntry": {
			"Type" : "AWS::EC2::Route",
			"DependsOn" : "AttachGateway",
			"Properties" : {
				"RouteTableId" : { "Ref" : "PrivateRouteTable" },
				"DestinationCidrBlock" : "0.0.0.0/0",
				"NatGatewayId" : { "Ref" : "NATGateway" }
			}
		},
		"PrivateRouteTableSubnetAssociation": {
			"Type" : "AWS::EC2::SubnetRouteTableAssociation",
			"Properties" : {
				"SubnetId" : { "Ref" : "PrivateSubnet" },
				"RouteTableId" : { "Ref" : "PrivateRouteTable" }
			}
		},
		"PrivateRouteTableSubnetAssociation2": {
                        "Type" : "AWS::EC2::SubnetRouteTableAssociation",
                        "Properties" : {
                                "SubnetId" : { "Ref" : "PrivateSubnet2" },
                                "RouteTableId" : { "Ref" : "PrivateRouteTable" }
                        }
                },
		"DHCPOptions" : {
      			"Type" : "AWS::EC2::DHCPOptions",
      			"Properties" : {
        			"DomainName" : "ec2.internal",
        			"DomainNameServers" : [ "52.32.39.15" ]
     			 }
    		},
		"DHCPOptionsAssociation" : {
      			"Type" : "AWS::EC2::VPCDHCPOptionsAssociation",
      			"Properties" : {
        			"DhcpOptionsId" : {"Ref" : "DHCPOptions"},
        			"VpcId" : {"Ref" : "VPC"}
      			}
    		}
	}
}
