export const BACKEND_ERROR_CODES = {
  DATABASE_OPERATION: 'DatabaseOperationException',
  EMAIL_ALREADY_CONFIRMED: 'EmailAlreadyConfirmedException',
  EMAIL_ALREADY_IN_USE: 'EmailAlreadyInUseException',
  EMAIL_NOT_CONFIRMED: 'EmailNotConfirmedException',
  EMAIL_NOT_REGISTERED: 'EmailNotRegisteredException',
  INVALID_CREDENTIALS: 'InvalidCredentialsException',
  TOKEN_EXPIRED: 'TokenExpiredException',
  TOKEN_INVALID: 'TokenInvalidException',
} as const;
