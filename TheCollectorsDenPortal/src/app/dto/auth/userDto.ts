import { UserRole } from './userRole';
import { UserStatus } from './userStatus';

export interface UserDto {
  userId: number;
  name: string;
  email: string;
  country: string;
  address: string;
  city: string;
  postalCode: string;
  phoneNumber: string;
  preferences: string;
  profilePictureUrl: string;
  createdAt: string;
  updatedAt: string;
  lastLogin: string;
  userStatus: UserStatus;
  userRole: UserRole;
}
