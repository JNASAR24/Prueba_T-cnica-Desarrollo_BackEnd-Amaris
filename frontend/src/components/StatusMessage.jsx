export default function StatusMessage({ message }) {
  if (!message) {
    return null;
  }
  return <footer className="status">{message}</footer>;
}
